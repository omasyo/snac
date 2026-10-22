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
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.TrendingItem
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
    onMediaClick: (Media) -> Unit,
    onAllChartsClick: () -> Unit,
    trendingTab: TrendingTab,
    onTrendingTabChange: (TrendingTab) -> Unit,
    trendingMovies: List<TrendingItem>,
    trendingShows: List<TrendingItem>,
    boxOffice: List<BoxOfficeItem>,
    anticipated: List<AnticipatedItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
        trendingMovies.firstOrNull()?.let { item ->
            TrendingSpotlight(
                title = item.media.title,
                imageUrl = item.media.imageUrl,
                watchingCount = item.watchers,
                onClick = { onMediaClick(item.media) },
            )
        }
        SignInPrompt(
            title = "TRACK WHAT YOU WATCH",
            message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
            onSignInClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TrendingSection(
            onMediaClick = onMediaClick,
            selectedTab = trendingTab,
            onTabChange = onTrendingTabChange,
            movies = trendingMovies,
            shows = trendingShows,
            modifier = Modifier
                .rotate(-1.5f),
        )

        BoxOfficeSection(
            onMovieClick = onMediaClick,
            items = boxOffice,
            modifier = Modifier.padding(16.dp),
        )
        AnticipatedSection(
            onMediaClick = onMediaClick,
            onAllChartsClick = onAllChartsClick,
            items = anticipated,
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
            onMediaClick = {},
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