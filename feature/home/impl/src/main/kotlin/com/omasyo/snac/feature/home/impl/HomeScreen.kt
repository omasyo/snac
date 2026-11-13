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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.RootScaffoldLayout
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.CuttingRoom
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun HomeScreen(
    onSignInClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onAllChartsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = metroViewModel(),
) {
    HomeScreen(
        onSignInClick = onSignInClick,
        onSettingsClick = onSettingsClick,
        onMediaClick = onMediaClick,
        onAllChartsClick = onAllChartsClick,
        topTrendingMedia = viewModel.topTrendingMedia.collectAsStateWithLifecycle().value,
        trendingMovies = viewModel.trendingMovies.collectAsStateWithLifecycle().value,
        trendingShows = viewModel.trendingShows.collectAsStateWithLifecycle().value,
        boxOffice = viewModel.boxOffice.collectAsStateWithLifecycle().value,
        anticipated = viewModel.anticipated.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    onSignInClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onAllChartsClick: () -> Unit,
    topTrendingMedia: UiState<TrendingItem?>,
    trendingMovies: UiState<List<TrendingItem>>,
    trendingShows: UiState<List<TrendingItem>>,
    boxOffice: UiState<List<BoxOfficeItem>>,
    anticipated: UiState<List<AnticipatedItem>>,
    modifier: Modifier = Modifier,
) {
    RootScaffoldLayout(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(SnacColors.page)
                .padding(top = 16.dp)
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
                        .silhouette(SnacColors.signalShadow),
                )
//            Button(
//                text = "SIGN IN",
//                onClick = onSignInClick,
//            )
                Button(
                    text = "SETTINGS",
                    onClick = onSettingsClick,
                )
            }
            topTrendingMedia.transpose()?.let {
                TrendingSpotlight(
                    state = it,
                    onClick = onMediaClick,
                )
            }
            SignInPrompt(
                title = "Track what you watch",
                message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
                onSignInClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TrendingSection(
                onMediaClick = onMediaClick,
                moviesState = trendingMovies,
                showsState = trendingShows,
                modifier = Modifier
                    .rotate(-1.5f),
            )

            BoxOfficeSection(
                onMovieClick = onMediaClick,
                state = boxOffice,
                modifier = Modifier.padding(16.dp),
            )
            AnticipatedSection(
                onMediaClick = onMediaClick,
                onAllChartsClick = onAllChartsClick,
                state = anticipated,
            )
        }
    }
}

@Preview(device = "spec:width=1080px,height=12340px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme(CuttingRoom.dark) {
        HomeScreen(
            onSignInClick = {},
            onSettingsClick = {},
            onMediaClick = {},
            onAllChartsClick = {},
            topTrendingMedia = UiState.Success(TrendingItem.MoviePreviewSample.first()),
            trendingMovies = UiState.Success(TrendingItem.MoviePreviewSample),
            trendingShows = UiState.Success(TrendingItem.ShowPreviewSample),
            boxOffice = UiState.Success(BoxOfficeItem.PreviewSample),
            anticipated = UiState.Error(SnacResult.Error.Unexpected) {},
        )
    }
}