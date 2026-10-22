package com.omasyo.snac.feature.charts.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.Chart
import com.omasyo.snac.feature.charts.impl.ChartsScreen
import com.omasyo.snac.feature.charts.impl.MediaTab
import com.omasyo.snac.feature.charts.impl.TimePeriod

fun EntryProviderScope<NavKey>.chartsEntry() {
    entry<ChartsNavKey> {
        var mediaTab by remember { mutableStateOf(MediaTab.Movie) }
        var selectedChart by remember { mutableStateOf(Chart.Trending) }
        var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
        ChartsScreen(
            onMediaClick = {},
            selectedMediaTab = mediaTab,
            onSelectedMediaChange = { mediaTab = it },
            selectedChart = selectedChart,
            onSelectedChartChange = { selectedChart = it },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = { selectedPeriod = it },
            results = TrendingItem.MoviePreviewSample,
        )
    }
}
