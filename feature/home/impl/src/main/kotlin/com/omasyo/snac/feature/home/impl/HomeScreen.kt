package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.AnticipatedMedia
import com.omasyo.snac.core.model.media.TrendingMedia
import com.omasyo.snac.core.model.media.movie.AnticipatedMovie
import com.omasyo.snac.core.model.media.movie.BoxOfficeMovie
import com.omasyo.snac.core.model.media.movie.Movie
import com.omasyo.snac.core.model.media.movie.TrendingMovie
import com.omasyo.snac.core.model.media.show.Show
import com.omasyo.snac.core.model.media.show.TrendingShow
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun HomeScreen(
    onSignInClick: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    onShowClick: (Show) -> Unit,
    onAllChartsClick: () -> Unit,
    trendingTab: TrendingTab,
    onTrendingTabChange: (TrendingTab) -> Unit,
    trendingMovies: List<TrendingMovie>,
    trendingShows: List<TrendingShow>,
    boxOfficeMovies: List<BoxOfficeMovie>,
    anticipatedMedia: List<AnticipatedMovie>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(SnacColors.page)
            .bottomBarPadding()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "snac",
                style = SnacTypography.headline,
                modifier = Modifier
                    .rotate(-3f)
                    .silhouette(SnacColors.signal),
            )
            Button(
                text = "SIGN IN",
                onClick = onSignInClick,
            )
        }
        trendingMovies.firstOrNull()?.let { movie ->
            TrendingSpotlight(
                title = movie.title,
                imageUrl = movie.imageUrl,
                watchingCount = 1204,
                onClick = { onMovieClick(movie) },
            )
        }
        SignInPrompt(
            title = "TRACK WHAT YOU WATCH",
            message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
            onSignInClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TrendingSection(
            onMovieClick = onMovieClick,
            onShowClick = onShowClick,
            selectedTab = trendingTab,
            onTabChange = onTrendingTabChange,
            movies = trendingMovies,
            shows = trendingShows,
            modifier = Modifier
                .rotate(-1.5f),
        )

        BoxOfficeSection(
            onMovieClick = onMovieClick,
            movies = boxOfficeMovies,
            modifier = Modifier.padding(16.dp),
        )
        AnticipatedSection(
            onMediaClick = {
                when (it) {
                    is Movie -> onMovieClick(it)
                    is Show -> onShowClick(it)
                }
            },
            onAllChartsClick = onAllChartsClick,
            media = anticipatedMedia,
        )
    }
}

@Preview(device = "spec:width=1080px,height=12340px,dpi=440")
@Composable
private fun Preview() {
    var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
    SnacTheme {
        HomeScreen(
            onSignInClick = {},
            onMovieClick = {},
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