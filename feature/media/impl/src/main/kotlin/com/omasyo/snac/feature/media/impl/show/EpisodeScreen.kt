package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.EpisodeType
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.ui.R as UiR
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.format.formatDayMonth
import com.omasyo.snac.core.ui.format.formatRuntime
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.modifiers.Orientation
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.common.CastSection
import com.omasyo.snac.feature.media.impl.common.MediaHeroContent
import com.omasyo.snac.feature.media.impl.common.MediaInfo
import com.omasyo.snac.feature.media.impl.common.MediaOverview
import com.omasyo.snac.feature.media.impl.common.MediaTagline
import com.omasyo.snac.feature.media.impl.common.ReviewSection
import com.omasyo.snac.feature.media.impl.components.RatingsSection
import com.omasyo.snac.core.data.models.Review
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun EpisodeScreen(
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onReviewClick: (Review) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EpisodeViewModel = metroViewModel(),
) {
    EpisodeScreen(
        onBackClick = onBackClick,
        onPersonClick = onPersonClick,
        onSeeAllCastClick = onSeeAllCastClick,
        onReviewClick = onReviewClick,
        onSeeAllReviewClick = onSeeAllReviewClick,
        onCommentClick = onCommentClick,
        showTitle = viewModel.showTitle.collectAsStateWithLifecycle().value,
        episode = viewModel.episode.collectAsStateWithLifecycle().value,
        ratings = viewModel.ratings.collectAsStateWithLifecycle().value,
        cast = viewModel.cast.collectAsStateWithLifecycle().value,
        review = viewModel.review.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

@Composable
fun EpisodeScreen(
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onReviewClick: (Review) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    showTitle: UiState<String>,
    episode: UiState<EpisodeDetails>,
    ratings: UiState<Ratings>,
    cast: UiState<List<Cast>>,
    review: UiState<List<Review>>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(SnacColors.page)
            .bottomBarPadding()
            .navigationBarsPadding(),
    ) {

        MediaHero(
            state = episode.map {
                MediaHeroContent(
                    title = it.displayTitle(),
                    posterUrl = it.screenshotUrl,
                    secondaryPosterUrl = null,
                )
            },
        )
        MediaTagline(
            state = showTitle,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        MediaInfo(
            state = episode.map { it.info() },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        episode.map { it.overview }.transpose()?.let {
            MediaOverview(
                state = it,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
        }
        SignInPrompt(
            title = "Track this episode",
            message = "Sign in to check in, rate it, mark it seen, and add it to your watchlist, favorites or lists.",
            onSignInClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        RatingsSection(
            state = ratings,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        CastSection(
            onPersonClick = onPersonClick,
            onSeeAllClick = onSeeAllCastClick,
            state = cast,
        )
        ReviewSection(
            onReviewClick = onReviewClick,
            onSeeAllClick = onSeeAllReviewClick,
            onCommentClick = onCommentClick,
            state = review,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
    IconButton(
        icon = UiR.drawable.caret_left_bold,
        contentDescription = stringResource(R.string.back),
        onClick = onBackClick,
        backgroundColor = SnacColors.chip,
        contentColor = SnacColors.ink,
        modifier = Modifier
            .statusBarsPadding()
            .padding(start = 16.dp, top = 16.dp)
            .size(48.dp),
    )


}


@Composable
private fun MediaHero(
    state: UiState<MediaHeroContent>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .film(
                orientation = Orientation.Horizontal,
                side = Side.Start,
            )
            .background(SnacColors.film)
    ) {
        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                    modifier = Modifier.matchParentSize(),
                ) {
                }
            }

            UiState.Loading -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart),
                ) {
                    Box(
                        Modifier
                            .size(240.dp, 40.dp)
                            .background(SnacColors.placeholder)
                    )
                }
            }

            is UiState.Success -> {
                val hero = state.content
                Image(
                    imageUrl = hero.secondaryPosterUrl ?: hero.posterUrl,
                    modifier = Modifier.matchParentSize(),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .rotate(if (hero.secondaryPosterUrl != null) -4f else 0f)
                        .padding(16.dp)
                        .align(Alignment.BottomStart),
                ) {
                    if (hero.secondaryPosterUrl != null) {
                        Image(
                            imageUrl = hero.posterUrl,
                            modifier = Modifier
                                .silhouette(SnacColors.signalShadow)
                                .border(4.dp, SnacColors.chip)
                                .size(112.dp, 168.dp),
                        )
                    }
                    Text(
                        text = hero.title,
                        style = SnacTypography.display.copy(
                            lineHeightStyle = CapHeightLineHeightStyle,
                            hyphens = Hyphens.Auto,
                        ),
                        color = SnacColors.onFilm,
                        modifier = Modifier
                            .weight(1f)
                            .silhouette(SnacColors.signalShadow)
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeDetails.info(): List<Pair<String, String>> = listOfNotNull(
    stringResource(R.string.info_season) to season.toString(),
    stringResource(R.string.info_episode) to number.toString(),
    stringResource(R.string.info_runtime) to runtime.formatRuntime(),
    firstAired?.let { stringResource(R.string.info_year) to it.year.toString() },
    firstAired?.let { stringResource(R.string.info_aired) to it.formatDayMonth() },
    type.label()?.let { stringResource(R.string.info_type) to it },
)

@Composable
private fun EpisodeType.label(): String? = when (this) {
    EpisodeType.Standard -> null
    EpisodeType.SeriesPremiere -> stringResource(R.string.episode_type_series_premiere)
    EpisodeType.SeasonPremiere -> stringResource(R.string.episode_type_season_premiere)
    EpisodeType.MidSeasonFinale -> stringResource(R.string.episode_type_mid_season_finale)
    EpisodeType.MidSeasonPremiere -> stringResource(R.string.episode_type_mid_season_premiere)
    EpisodeType.SeasonFinale -> stringResource(R.string.episode_type_season_finale)
    EpisodeType.SeriesFinale -> stringResource(R.string.episode_type_series_finale)
}

@Preview(device = "spec:width=1080px,height=6000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        EpisodeScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = UiState.Success("Dark"),
            episode = UiState.Success(EpisodeDetails.PreviewSample.first()),
            ratings = UiState.Success(Ratings.PreviewSample),
            cast = UiState.Success(Cast.PreviewSample),
            review = UiState.Success(Review.PreviewSample.take(2)),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=6000px,dpi=440")
@Composable
private fun LoadingPreview() {
    SnacTheme {
        EpisodeScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = UiState.Loading,
            episode = UiState.Loading,
            ratings = UiState.Loading,
            cast = UiState.Loading,
            review = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=6000px,dpi=440")
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        EpisodeScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = error,
            episode = error,
            ratings = error,
            cast = error,
            review = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
