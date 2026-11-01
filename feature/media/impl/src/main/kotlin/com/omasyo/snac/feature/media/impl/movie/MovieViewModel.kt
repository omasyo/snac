package com.omasyo.snac.feature.media.impl.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.movie.MovieRepository
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.feature.media.impl.common.RefreshWrapper
import com.omasyo.snac.feature.media.impl.common.mock
import com.omasyo.snac.feature.media.impl.components.Review
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class MovieViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {
    val movie: StateFlow<UiState<MovieDetails>> =
        RefreshWrapper(viewModelScope) { mock(MovieDetails.PreviewSample) }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { mock(Ratings.PreviewSample) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { mock(Cast.PreviewSample) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) { mock(Review.PreviewSample.take(2)) }.response

    val relatedMovies: StateFlow<UiState<List<Movie>>> =
        RefreshWrapper(viewModelScope) { mock(Movie.PreviewSample) }.response
}
