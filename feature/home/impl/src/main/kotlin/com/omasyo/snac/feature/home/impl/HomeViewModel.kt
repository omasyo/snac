package com.omasyo.snac.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.media.MediaRepository
import com.omasyo.snac.core.data.movie.MovieRepository
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class HomeViewModel(
    private val mediaRepository: MediaRepository,
    private val movieRepository: MovieRepository,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val topTrendingMedia: StateFlow<UiState<TrendingItem?>> =
        RefreshWrapper(viewModelScope) { mediaRepository.getTopTrending() }.response

    val trendingMovies: StateFlow<UiState<List<TrendingItem>>> =
        RefreshWrapper(viewModelScope) { movieRepository.getTopTrending() }.response

    val trendingShows: StateFlow<UiState<List<TrendingItem>>> =
        RefreshWrapper(viewModelScope) { showRepository.getTopTrending() }.response

    val boxOffice: StateFlow<UiState<List<BoxOfficeItem>>> =
        RefreshWrapper(viewModelScope) { movieRepository.getBoxOffice() }.response

    val anticipated: StateFlow<UiState<List<AnticipatedItem>>> =
        RefreshWrapper(viewModelScope) { mediaRepository.getAnticipated() }.response
}
