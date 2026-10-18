package com.omasyo.snac.feature.home

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : TopLevelRoute {
    override val label: String = "Home"
}


fun EntryProviderScope<NavKey>.homeNavigation() {
    entry<HomeRoute> {
        HomeScreen(
            onSignInClick = {},
            onMovieClick = {},
            onShowClick = {},
            onAllChartsClick = {},
            trendingTab = TrendingTab.Movie,
            onTrendingTabClick = {},
        )
    }
}