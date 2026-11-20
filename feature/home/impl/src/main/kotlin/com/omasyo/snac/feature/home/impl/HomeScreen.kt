package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.PlaybackItem
import com.omasyo.snac.core.model.media.Recommendation
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.model.media.UpNextItem
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.RootScaffoldLayout
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.CuttingRoom
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.auth.api.ProvideSignInState
import com.omasyo.snac.feature.auth.api.components.SignInGate
import com.omasyo.snac.feature.auth.api.components.SignInPrompt
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun HomeScreen(
    onSettingsClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onEpisodeClick: (Show, Episode) -> Unit,
    onAllChartsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = metroViewModel(),
) {
    HomeScreen(
        onSettingsClick = onSettingsClick,
        onMediaClick = onMediaClick,
        onEpisodeClick = onEpisodeClick,
        onAllChartsClick = onAllChartsClick,
        topTrendingMedia = viewModel.topTrendingMedia.collectAsStateWithLifecycle().value,
        trendingMovies = viewModel.trendingMovies.collectAsStateWithLifecycle().value,
        trendingShows = viewModel.trendingShows.collectAsStateWithLifecycle().value,
        boxOffice = viewModel.boxOffice.collectAsStateWithLifecycle().value,
        anticipated = viewModel.anticipated.collectAsStateWithLifecycle().value,
        upNext = viewModel.upNext.collectAsStateWithLifecycle().value,
        playback = viewModel.playback.collectAsStateWithLifecycle().value,
        recommendedMovies = viewModel.recommendedMovies.collectAsStateWithLifecycle().value,
        recommendedShows = viewModel.recommendedShows.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    onSettingsClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onEpisodeClick: (Show, Episode) -> Unit,
    onAllChartsClick: () -> Unit,
    topTrendingMedia: UiState<TrendingItem?>,
    trendingMovies: UiState<List<TrendingItem>>,
    trendingShows: UiState<List<TrendingItem>>,
    boxOffice: UiState<List<BoxOfficeItem>>,
    anticipated: UiState<List<AnticipatedItem>>,
    upNext: UiState<List<UpNextItem>>,
    playback: UiState<List<PlaybackItem>>,
    recommendedMovies: UiState<List<Recommendation>>,
    recommendedShows: UiState<List<Recommendation>>,
    modifier: Modifier = Modifier,
) {
    RootScaffoldLayout(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(SnacColors.page)
                .padding(vertical = 16.dp)
                .bottomBarPadding()
                .statusBarsPadding(),
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
                SignInGate(
                    signedOutContent = {
                        Button(
                            text = "SETTINGS",
                            onClick = onSettingsClick,
                        )
                    }
                ) {
                    val indicationColor = SnacColors.accent
                    Image(
                        imageUrl = null,//TODO
                        modifier = Modifier
                            .clickable(
                                indication = TintIndication(true) { indicationColor },
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onSettingsClick,
                            )
                            .size(48.dp)
//                            .border(2.dp, SnacColors.outline, CircleShape)
                            .clip(CircleShape),
                    )
                }
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
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.bleedHorizontally(16.dp),
                ) {
                    upNext.map { it.takeIf { items -> items.isNotEmpty() } }.transpose()?.let {
                        UpNextSection(
                            onEpisodeClick = onEpisodeClick,
                            state = it,
                        )
                    }
                    playback.map { it.takeIf { items -> items.isNotEmpty() } }.transpose()?.let {
                        ContinueWatchingSection(
                            onMediaClick = onMediaClick,
                            onEpisodeClick = onEpisodeClick,
                            state = it,
                        )
                    }
                }
            }
            TrendingSection(
                onMediaClick = onMediaClick,
                moviesState = trendingMovies,
                showsState = trendingShows,
                modifier = Modifier
                    .rotate(-1.5f),
            )

            SignInGate {
                RecommendationsSection(
                    onMediaClick = onMediaClick,
                    moviesState = recommendedMovies,
                    showsState = recommendedShows,
                )
            }
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

@Preview(device = "spec:width=1080px,height=6340px,dpi=440")
@Composable
private fun Preview() {
    ProvideSignInState(true) {
        SnacTheme(CuttingRoom.light) {
            HomeScreen(
                onSettingsClick = {},
                onMediaClick = {},
                onEpisodeClick = { _, _ -> },
                onAllChartsClick = {},
                topTrendingMedia = UiState.Success(TrendingItem.MoviePreviewSample.first()),
                trendingMovies = UiState.Success(TrendingItem.MoviePreviewSample),
                trendingShows = UiState.Success(TrendingItem.ShowPreviewSample),
                boxOffice = UiState.Success(BoxOfficeItem.PreviewSample),
                anticipated = UiState.Error(SnacResult.Error.Unexpected) {},
                upNext = UiState.Success(UpNextItem.PreviewSample),
                playback = UiState.Success(PlaybackItem.PreviewSample),
                recommendedMovies = UiState.Success(Recommendation.MoviePreviewSample),
                recommendedShows = UiState.Success(Recommendation.ShowPreviewSample),
            )
        }
    }
}