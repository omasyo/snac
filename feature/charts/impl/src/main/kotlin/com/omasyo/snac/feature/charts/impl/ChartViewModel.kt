package com.omasyo.snac.feature.charts.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ChartViewModel(

) : ViewModel() {
    var mediaTab by mutableStateOf(MediaTab.Movie) // TODO probably save what the user last selected to store? for all
        private set

    fun changeMediaTab(mediaTab: MediaTab) {
        this.mediaTab = mediaTab
    }

    var selectedChart by mutableStateOf(Chart.Trending)
        private set

    fun changeChart(chart: Chart) {
        selectedChart = chart
    }

    var selectedPeriod by mutableStateOf(TimePeriod.Weekly)
        private set

    fun changePeriod(period: TimePeriod) {
        selectedPeriod = period
    }

    val results: Flow<PagingData<ChartItem>> =
        flowOf(PagingData.from(TrendingItem.MoviePreviewSample))

}