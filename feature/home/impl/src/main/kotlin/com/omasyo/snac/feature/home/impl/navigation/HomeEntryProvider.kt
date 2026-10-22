package com.omasyo.snac.feature.home.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.feature.auth.api.rememberAuthenticator
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.home.impl.HomeScreen
import com.omasyo.snac.feature.home.impl.TrendingTab

fun EntryProviderScope<NavKey>.homeEntry() {
    entry<HomeNavKey> {
        val authenticator = rememberAuthenticator()

        var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
        HomeScreen(
            onSignInClick = { authenticator.signIn() },
            onMediaClick = {
                when (it) {
                    is Movie -> {}
                    is Show -> {}
                }
            },
            onAllChartsClick = {},
            trendingTab = trendingTab,
            onTrendingTabChange = { trendingTab = it },
            trendingMovies = TrendingItem.MoviePreviewSample,
            trendingShows = TrendingItem.ShowPreviewSample,
            boxOffice = BoxOfficeItem.PreviewSample,
            anticipated = AnticipatedItem.PreviewSample,
        )
    }
}
