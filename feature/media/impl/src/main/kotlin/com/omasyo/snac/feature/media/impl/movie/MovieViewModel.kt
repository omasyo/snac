package com.omasyo.snac.feature.media.impl.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.movie.MovieRepository
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.mock
import com.omasyo.snac.core.data.models.Review
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
class MovieViewModel(
    @Assisted private val slug: String,
    private val movieRepository: MovieRepository,
) : ViewModel() {
    val movie: StateFlow<UiState<MovieDetails>> =
        RefreshWrapper(viewModelScope) { movieRepository.getDetails(slug) }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { movieRepository.getRatings(slug) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { movieRepository.getCast(slug) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) { movieRepository.getTopComments(slug) }.response

    val relatedMovies: StateFlow<UiState<List<Movie>>> =
        RefreshWrapper(viewModelScope) { mock(Movie.PreviewSample) }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted slug: String): MovieViewModel
    }
}
