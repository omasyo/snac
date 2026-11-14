package com.omasyo.snac.feature.charts.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.ChartsScreen
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey

fun EntryProviderScope<NavKey>.chartsEntry(navigator: Navigator) {
    entry<ChartsNavKey> {
        ChartsScreen(
            onMediaClick = {
                when (it) {
                    is Movie -> navigator.navigate(MovieNavKey(it.slug))
                    is Show -> navigator.navigate(ShowNavKey(it.slug))
                }
            },
        )
    }
}
