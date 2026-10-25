package com.omasyo.snac.feature.charts.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.ChartsScreen

fun EntryProviderScope<NavKey>.chartsEntry() {
    entry<ChartsNavKey> {
        ChartsScreen(
            onMediaClick = {},
        )
    }
}
