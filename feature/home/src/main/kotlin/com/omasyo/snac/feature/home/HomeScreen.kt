package com.omasyo.snac.feature.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

enum class TrendingTab {
    Movie, Show
}

@Composable
fun HomeScreen(
    onSignInClick: () -> Unit,
    onMovieClick: () -> Unit,
    onShowClick: () -> Unit,
    onAllChartsClick: () -> Unit,
    trendingTab: TrendingTab,
    onTrendingTabClick: (TrendingTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(SnacColors.page)
            .bottomBarPadding()
//            .padding(vertical = 24.dp)
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
        TrendingSpotlight(
            title = "FRANKENSTEIN",
            watchingCount = 1204,
            onClick = {},
        )
        SignInPrompt(
            title = "TRACK WHAT YOU WATCH",
            message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
            onSignInClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TrendingSection(
            modifier = Modifier
                .rotate(-1.5f)
        )

        BoxOfficeSection(
            onMovieClick = {},
            movies = listOf(
                BoxOfficeMovie("Harmattan", "$21.7M"),
                BoxOfficeMovie("Orbital Diner", "$12.9M"),
                BoxOfficeMovie("Ghost Frequency", "$8.4"),
                BoxOfficeMovie("Velvet Hour", "$5.1M"),
                BoxOfficeMovie("Saltline", "$3.3M"),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(device = "spec:width=1080px,height=12340px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
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