package com.omasyo.snac.feature.charts.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.movie.MovieRepository
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.ChartItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ChartViewModel(
    movieRepository: MovieRepository,
    showRepository: ShowRepository,
) : ViewModel() {
    private var anchorPosition = 0

    val mediaTab: StateFlow<MediaTab> field = MutableStateFlow(MediaTab.Movie) // TODO probably save what the user last selected to store? for all

    fun changeMediaTab(mediaTab: MediaTab, anchorPosition: Int) {
        this.anchorPosition = anchorPosition
        this.mediaTab.value = mediaTab
    }

    val selectedChart: StateFlow<Chart> field = MutableStateFlow(Chart.Trending)

    fun changeChart(chart: Chart, anchorPosition: Int) {
        this.anchorPosition = anchorPosition
        selectedChart.value = chart
    }

    val selectedPeriod: StateFlow<TimePeriod> field = MutableStateFlow(TimePeriod.Weekly)

    fun changePeriod(period: TimePeriod, anchorPosition: Int) {
        this.anchorPosition = anchorPosition
        selectedPeriod.value = period
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val results: Flow<PagingData<ChartItem>> = combine(
        mediaTab,
        selectedChart,
        selectedPeriod,
        ::Triple,
    ).distinctUntilChanged().flatMapLatest { (mediaTab, selectedChart, selectedPeriod) ->
        val isMovie = mediaTab == MediaTab.Movie
        when (selectedChart) {
            Chart.Trending if isMovie -> movieRepository.getTrending(anchorPosition)
            Chart.Trending -> showRepository.getTrending(anchorPosition)
            Chart.Popular if isMovie -> movieRepository.getPopular(anchorPosition)
            Chart.Popular -> showRepository.getPopular(anchorPosition)
            Chart.Anticipated if isMovie -> movieRepository.getAnticipated(anchorPosition)
            Chart.Anticipated -> showRepository.getAnticipated(anchorPosition)
            Chart.Watched if isMovie -> movieRepository.getMostWatched(selectedPeriod, anchorPosition)
            Chart.Watched -> showRepository.getMostWatched(selectedPeriod, anchorPosition)
            Chart.Played if isMovie -> movieRepository.getMostPlayed(selectedPeriod, anchorPosition)
            Chart.Played -> showRepository.getMostPlayed(selectedPeriod, anchorPosition)
            Chart.Collected if isMovie -> movieRepository.getMostCollected(selectedPeriod, anchorPosition)
            Chart.Collected -> showRepository.getMostCollected(selectedPeriod, anchorPosition)
            Chart.Favorited if isMovie -> movieRepository.getMostFavorited(selectedPeriod, anchorPosition)
            Chart.Favorited -> showRepository.getMostFavorited(selectedPeriod, anchorPosition)
            Chart.BoxOffice -> movieRepository.getBoxOfficePaged()
        }
    }.cachedIn(viewModelScope)
}