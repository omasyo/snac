package com.omasyo.snac.feature.charts.impl

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
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
import kotlinx.coroutines.flow.flatMapLatest

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ChartViewModel(
    movieRepository: MovieRepository,
    showRepository: ShowRepository,
) : ViewModel() {
    val mediaTab: StateFlow<MediaTab> field = MutableStateFlow(MediaTab.Movie) // TODO probably save what the user last selected to store? for all

    fun changeMediaTab(mediaTab: MediaTab) {
        this.mediaTab.value = mediaTab
    }

    val selectedChart: StateFlow<Chart> field = MutableStateFlow(Chart.Trending)

    fun changeChart(chart: Chart) {
        selectedChart.value = chart
    }

    val selectedPeriod: StateFlow<TimePeriod> field = MutableStateFlow(TimePeriod.Weekly)

    fun changePeriod(period: TimePeriod) {
        selectedPeriod.value = period
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val results: Flow<PagingData<ChartItem>> = combine(
        mediaTab,
        selectedChart,
        selectedPeriod,
        ::Triple,
    ).flatMapLatest { (mediaTab, selectedChart, selectedPeriod) ->
        val isMovie = mediaTab == MediaTab.Movie
        when (selectedChart) {
            Chart.Trending if isMovie -> movieRepository.getTrending()
            Chart.Trending -> showRepository.getTrending()
            Chart.Popular if isMovie -> movieRepository.getPopular()
            Chart.Popular -> showRepository.getPopular()
            Chart.Anticipated if isMovie -> movieRepository.getAnticipated()
            Chart.Anticipated -> showRepository.getAnticipated()
            Chart.Watched if isMovie -> movieRepository.getMostWatched(selectedPeriod)
            Chart.Watched -> showRepository.getMostWatched(selectedPeriod)
            Chart.Played if isMovie -> movieRepository.getMostPlayed(selectedPeriod)
            Chart.Played -> showRepository.getMostPlayed(selectedPeriod)
            Chart.Collected if isMovie -> movieRepository.getMostCollected(selectedPeriod)
            Chart.Collected -> showRepository.getMostCollected(selectedPeriod)
            Chart.Favorited if isMovie -> movieRepository.getMostFavorited(selectedPeriod)
            Chart.Favorited -> showRepository.getMostFavorited(selectedPeriod)
            Chart.BoxOffice -> movieRepository.getBoxOfficePaged()
        }

    }
//    {mediaTab, selectedChart, selectedPeriod ->
//        Triple(mediaTab, selectedChart, selectedPeriod)
//    }
}