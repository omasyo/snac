package com.omasyo.snac.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.auth.api.rememberAuthenticator
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.home.impl.HomeScreen
import com.omasyo.snac.feature.home.impl.TrendingTab

fun EntryProviderScope<NavKey>.homeEntry() {
    entry<HomeNavKey> {
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
