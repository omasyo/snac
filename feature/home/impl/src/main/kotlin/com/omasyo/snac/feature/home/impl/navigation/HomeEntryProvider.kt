package com.omasyo.snac.feature.home.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.AnticipatedMedia
import com.omasyo.snac.core.model.media.TrendingMedia
import com.omasyo.snac.core.model.media.movie.BoxOfficeMovie
import com.omasyo.snac.core.model.media.show.TrendingShow
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
            onMovieClick = { authenticator.signOut() },
            onShowClick = {},
            onAllChartsClick = {},
            trendingTab = trendingTab,
            onTrendingTabChange = { trendingTab = it },
            trendingMovies = TrendingMedia.PreviewSample,
            trendingShows = TrendingShow.PreviewSample,
            boxOfficeMovies = BoxOfficeMovie.PreviewSample,
            anticipatedMedia = AnticipatedMedia.PreviewSample
        )
    }
}
