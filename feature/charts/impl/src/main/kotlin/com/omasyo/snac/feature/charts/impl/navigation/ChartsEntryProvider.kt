package com.omasyo.snac.feature.charts.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.ChartsScreen

fun EntryProviderScope<NavKey>.chartsEntry(
    onMediaClick: (Media) -> Unit,
) {
    entry<ChartsNavKey> {
        ChartsScreen(
            onMediaClick = onMediaClick,
        )
    }
}
