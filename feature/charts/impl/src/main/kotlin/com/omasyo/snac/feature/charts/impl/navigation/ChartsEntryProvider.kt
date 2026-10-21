package com.omasyo.snac.feature.charts.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.ChartsScreen
import com.omasyo.snac.feature.charts.impl.TrendingTab

fun EntryProviderScope<NavKey>.chartsEntry() {
    entry<ChartsNavKey> {
        var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
        ChartsScreen(
            tab = trendingTab,
            onTabChange = { trendingTab = it },
        )
    }
}
