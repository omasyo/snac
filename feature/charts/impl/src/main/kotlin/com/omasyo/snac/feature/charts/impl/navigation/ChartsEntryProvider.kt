package com.omasyo.snac.feature.charts.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.Chart
import com.omasyo.snac.feature.charts.impl.ChartsScreen
import com.omasyo.snac.feature.charts.impl.Media
import com.omasyo.snac.feature.charts.impl.TimePeriod

fun EntryProviderScope<NavKey>.chartsEntry() {
    entry<ChartsNavKey> {

        var media by remember { mutableStateOf(Media.Movie) }
        var expanded by remember { mutableStateOf(true) }
        var selectedChart by remember { mutableStateOf(Chart.Trending) }
        var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
        ChartsScreen(
            onMovieClick = {},
            onShowClick = {},
            selectedMedia = media,
            onSelectedMediaChange = { media = it },
            selectedChart = selectedChart,
            onSelectedChartChange = {
                if (!expanded) {
                    expanded = true
                } else {
                    selectedChart = it
                    expanded = false
                }
            },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = {
                if (!expanded) {
                    expanded = true
                } else {
                    selectedPeriod = it
                    expanded = false
                }
            },
            selectorExpanded = expanded,
        )
    }
}
