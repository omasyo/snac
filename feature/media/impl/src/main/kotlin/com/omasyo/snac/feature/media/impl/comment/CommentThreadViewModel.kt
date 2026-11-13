package com.omasyo.snac.feature.media.impl.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.comment.CommentRepository
import com.omasyo.snac.core.data.models.Reply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.Flow

@AssistedInject
class CommentThreadViewModel(
    @Assisted private val commentId: Long,
    commentRepository: CommentRepository,
) : ViewModel() {
    val replies: Flow<PagingData<Reply>> =
        commentRepository.getReplies(commentId).cachedIn(viewModelScope)

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted commentId: Long): CommentThreadViewModel
    }
}
