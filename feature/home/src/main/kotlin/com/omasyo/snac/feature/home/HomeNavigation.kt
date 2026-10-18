package com.omasyo.snac.feature.home

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import com.omasyo.snac.feature.auth.api.rememberAuthenticator
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : TopLevelRoute {
    override val label: String = "Home"
}


fun EntryProviderScope<NavKey>.homeNavigation() {
    entry<HomeRoute> {
        val authenticator = rememberAuthenticator()

        HomeScreen(
            onSignInClick = { authenticator.signIn() },
            onMovieClick = { authenticator.signOut() },
            onShowClick = {},
            onAllChartsClick = {},
            trendingTab = TrendingTab.Movie,
            onTrendingTabClick = {},
        )
    }
}