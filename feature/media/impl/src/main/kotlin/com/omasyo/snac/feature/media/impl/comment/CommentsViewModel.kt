package com.omasyo.snac.feature.media.impl.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.comment.CommentRepository
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Genre
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.data.movie.MovieRepository
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.model.extra.CommentSort
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.feature.media.api.navigation.CommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieCommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowCommentsNavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AssistedInject
class CommentsViewModel(
    @Assisted private val key: CommentsNavKey,
    private val movieRepository: MovieRepository,
    private val showRepository: ShowRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    val hero: StateFlow<UiState<HeroContent>> = RefreshWrapper(viewModelScope) {
        when (key) {
            is MovieCommentsNavKey -> movieRepository.getDetails(key.slug).map {
                heroContent(it.title, it.posterUrl, it.secondaryPosterUrl, it.year, it.genres)
            }

            is ShowCommentsNavKey -> showRepository.getDetails(key.slug).map {
                heroContent(it.title, it.posterUrl, it.secondaryPosterUrl, it.year, it.genres)
            }
        }
    }.response

    val comments: Flow<PagingData<Comment>> = when (key) {
        is MovieCommentsNavKey -> movieRepository.getComments(key.slug, CommentSort.Likes)
        is ShowCommentsNavKey -> showRepository.getComments(key.slug, CommentSort.Likes)
    }.cachedIn(viewModelScope)

    val topReplies: StateFlow<Map<Long, UiState<List<Reply>>>>
        field = MutableStateFlow(emptyMap())

    fun loadTopReplies(comment: Comment) {
        if (comment.repliesCount == 0 || comment.id in topReplies.value) return
        fetchTopReplies(comment.id)
    }

    private fun fetchTopReplies(commentId: Long) {
        topReplies.update { it + (commentId to UiState.Loading) }
        viewModelScope.launch {
            val state = when (val result = commentRepository.getTopReplies(commentId)) {
                is SnacResult.Failure -> UiState.Error(result.error) { fetchTopReplies(commentId) }
                is SnacResult.Success -> UiState.Success(result.value)
            }
            topReplies.update { it + (commentId to state) }
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted key: CommentsNavKey): CommentsViewModel
    }
}


private fun heroContent(
    title: String,
    posterUrl: String?,
    secondaryPosterUrl: String?,
    year: Int?,
    genres: List<Genre>,
) = HeroContent(
    title = title,
    posterUrl = posterUrl,
    secondaryPosterUrl = secondaryPosterUrl,
    extra = metaLine(
        year?.toString(),
        genres.take(2).joinToString(", ") { it.name }.takeIf { it.isNotEmpty() },
    ),
)
