package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.heroScrollable
import com.omasyo.snac.core.ui.format.formatDayMonth
import com.omasyo.snac.core.ui.format.formatRuntime
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.mapOrNull
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.common.CastSection
import com.omasyo.snac.feature.media.impl.common.MediaDetailsLayout
import com.omasyo.snac.feature.media.impl.common.MediaHero
import com.omasyo.snac.feature.media.impl.common.MediaHeroContent
import com.omasyo.snac.feature.media.impl.common.MediaInfo
import com.omasyo.snac.feature.media.impl.common.MediaOverview
import com.omasyo.snac.feature.media.impl.common.MediaTagline
import com.omasyo.snac.feature.media.impl.common.ReviewSection
import com.omasyo.snac.feature.media.impl.components.RatingsSection
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun SeasonScreen(
    showSlug: String,
    season: Int,
    onBackClick: () -> Unit,
    onEpisodeClick: (EpisodeDetails) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onReviewClick: (Comment) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SeasonViewModel = assistedMetroViewModel<SeasonViewModel, SeasonViewModel.Factory> {
        create(showSlug, season)
    },
) {
    SeasonScreen(
        onBackClick = onBackClick,
        onEpisodeClick = onEpisodeClick,
        onPersonClick = onPersonClick,
        onSeeAllCastClick = onSeeAllCastClick,
        onReviewClick = onReviewClick,
        onSeeAllReviewClick = onSeeAllReviewClick,
        onCommentClick = onCommentClick,
        showTitle = viewModel.showTitle.collectAsStateWithLifecycle().value,
        season = viewModel.season.collectAsStateWithLifecycle().value,
        ratings = viewModel.ratings.collectAsStateWithLifecycle().value,
        episodes = viewModel.episodes.collectAsStateWithLifecycle().value,
        cast = viewModel.cast.collectAsStateWithLifecycle().value,
        comment = viewModel.comment.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

@Composable
fun SeasonScreen(
    onBackClick: () -> Unit,
    onEpisodeClick: (EpisodeDetails) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onReviewClick: (Comment) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    showTitle: UiState<String>,
    season: UiState<Season>,
    ratings: UiState<Ratings>,
    episodes: UiState<List<EpisodeDetails>>,
    cast: UiState<List<Cast>>,
    comment: UiState<List<Comment>>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    MediaDetailsLayout(
        onBackClick = onBackClick,
        modifier = modifier,
        title = season.mapOrNull { it.title } ?: "",
        hero = {
            MediaHero(
                state = season.map {
                    MediaHeroContent(
                        title = it.title,
                        posterUrl = it.posterUrl,
                        secondaryPosterUrl = null,
                    )
                },
                modifier = Modifier.heroScrollable(scrollState),
            )
        }
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(contentPadding)
                .padding(top = 16.dp),
        ) {
            MediaTagline(
                state = showTitle,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            MediaInfo(
                state = season.map { it.info() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
            season.map { it.overview }.transpose()?.let {
                MediaOverview(
                    state = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )
            }
            SignInPrompt(
                title = "Track this season",
                message = "Sign in to check in, rate it, mark it seen, and add it to your watchlist, favorites or lists.",
                onSignInClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            RatingsSection(
                state = ratings,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            EpisodesSection(
                onEpisodeClick = onEpisodeClick,
                state = episodes,
                modifier = Modifier.padding(horizontal = 16.dp)
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
                state = comment,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }
    }
}

@Composable
private fun Season.info(): List<Pair<String, String>> = listOfNotNull(
    stringResource(R.string.info_episodes) to
            if (airedEpisodes < episodeCount) stringResource(
                R.string.aired_of_total,
                airedEpisodes,
                episodeCount
            )
            else episodeCount.toString(),
    totalRuntime?.let { stringResource(R.string.info_runtime) to it.formatRuntime() },
    firstAired?.let { stringResource(R.string.info_year) to it.year.toString() },
    firstAired?.let { stringResource(R.string.info_premiered) to it.formatDayMonth() },
    network?.let { stringResource(R.string.info_network) to it },
)

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        SeasonScreen(
            onBackClick = {},
            onEpisodeClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = UiState.Success("Dark"),
            season = UiState.Success(
                Season.PreviewSample.first()
                    .copy(overview = "A missing child brings four families together, and the search leads somewhere none of them expected.")
            ),
            ratings = UiState.Success(Ratings.PreviewSample),
            episodes = UiState.Success(EpisodeDetails.PreviewSample),
            cast = UiState.Success(Cast.PreviewSample),
            comment = UiState.Success(Comment.PreviewSample.take(2)),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun LoadingPreview() {
    SnacTheme {
        SeasonScreen(
            onBackClick = {},
            onEpisodeClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = UiState.Loading,
            season = UiState.Loading,
            ratings = UiState.Loading,
            episodes = UiState.Loading,
            cast = UiState.Loading,
            comment = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        SeasonScreen(
            onBackClick = {},
            onEpisodeClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            showTitle = error,
            season = error,
            ratings = error,
            episodes = error,
            cast = error,
            comment = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
