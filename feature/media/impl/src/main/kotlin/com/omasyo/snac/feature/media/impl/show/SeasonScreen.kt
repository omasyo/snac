package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.format.formatDayMonth
import com.omasyo.snac.core.ui.format.formatRuntime
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
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
import com.omasyo.snac.feature.media.impl.components.Review

@Composable
fun SeasonScreen(
    onBackClick: () -> Unit,
    onEpisodeClick: (EpisodeDetails) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onReviewClick: (Review) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    showTitle: UiState<String>,
    season: UiState<Season>,
    ratings: UiState<Ratings>,
    episodes: UiState<List<EpisodeDetails>>,
    cast: UiState<List<Cast>>,
    review: UiState<List<Review>>,
    modifier: Modifier = Modifier,
) {
    MediaDetailsLayout(onBackClick = onBackClick, modifier = modifier) {
        MediaHero(
            state = season.map {
                MediaHeroContent(
                    title = it.title,
                    posterUrl = it.posterUrl,
                    secondaryPosterUrl = null,
                )
            },
        )
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
            state = review,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun Season.info(): List<Pair<String, String>> = listOfNotNull(
    stringResource(R.string.info_episodes) to
        if (airedEpisodes < episodeCount) stringResource(R.string.aired_of_total, airedEpisodes, episodeCount)
        else episodeCount.toString(),
    stringResource(R.string.info_runtime) to totalRuntime.formatRuntime(),
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
            review = UiState.Success(Review.PreviewSample.take(2)),
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
            review = UiState.Loading,
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
            review = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
