package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.data.models.ShowStatus
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.feature.media.impl.common.CastSection
import com.omasyo.snac.feature.media.impl.common.MediaDetailsLayout
import com.omasyo.snac.feature.media.impl.common.MediaGenres
import com.omasyo.snac.feature.media.impl.common.MediaHero
import com.omasyo.snac.feature.media.impl.common.MediaHeroContent
import com.omasyo.snac.feature.media.impl.common.MediaInfo
import com.omasyo.snac.feature.media.impl.common.MediaOverview
import com.omasyo.snac.feature.media.impl.common.MediaTagline
import com.omasyo.snac.feature.media.impl.common.RelatedSection
import com.omasyo.snac.feature.media.impl.common.ReviewSection
import com.omasyo.snac.feature.media.impl.common.toRuntimeLabel
import com.omasyo.snac.feature.media.impl.components.RatingsSection
import com.omasyo.snac.feature.media.impl.components.Review
import java.time.format.DateTimeFormatter

@Composable
fun ShowScreen(
    onBackClick: () -> Unit,
    onSeasonClick: (Season) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onReviewClick: (Review) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSeeAllRelatedClick: () -> Unit,
    show: UiState<ShowDetails>,
    ratings: UiState<Ratings>,
    seasons: UiState<List<Season>>,
    cast: UiState<List<Cast>>,
    review: UiState<List<Review>>,
    relatedShows: UiState<List<Show>>,
    modifier: Modifier = Modifier,
) {
    MediaDetailsLayout(onBackClick = onBackClick, modifier = modifier) {
        MediaHero(
            state = show.map {
                MediaHeroContent(
                    title = it.title,
                    posterUrl = it.posterUrl,
                    secondaryPosterUrl = it.secondaryPosterUrl,
                )
            },
        )
        show.map { it.tagline }.transpose()?.let {
            MediaTagline(
                state = it,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        MediaInfo(
            state = show.map { it.info() },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        MediaGenres(
            state = show.map { it.genres },
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        MediaOverview(
            state = show.map { it.overview },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        SignInPrompt(
            title = "Track this show",
            message = "Sign in to check in, rate it, mark it seen, and add it to your watchlist, favorites or lists.",
            onSignInClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        RatingsSection(
            state = ratings,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        SeasonsSection(
            onSeasonClick = onSeasonClick,
            state = seasons,
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
        )
        RelatedSection(
            onMediaClick = onMediaClick,
            onSeeAllClick = onSeeAllRelatedClick,
            state = relatedShows,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

private fun ShowDetails.info(): List<Pair<String, String>> = listOfNotNull(
    year?.let { "Year" to it.toString() },
    "Runtime" to runtime.toRuntimeLabel(),
    certification?.let { "Cert" to it },
    "Status" to status.label(),
    network?.let { "Network" to it },
    "Episodes" to airedEpisodes.toString(),
    firstAired?.let { "Premiered" to it.format(DateTimeFormatter.ofPattern("d MMM")) },
    "Country" to country.uppercase(),
    "Language" to language.uppercase(),
)

private fun ShowStatus.label(): String = when (this) {
    ShowStatus.ReturningSeries -> "Returning"
    ShowStatus.Continuing -> "Continuing"
    ShowStatus.InProduction -> "In Production"
    ShowStatus.Planned -> "Planned"
    ShowStatus.Upcoming -> "Upcoming"
    ShowStatus.Pilot -> "Pilot"
    ShowStatus.Canceled -> "Canceled"
    ShowStatus.Ended -> "Ended"
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        ShowScreen(
            onBackClick = {},
            onSeasonClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            show = UiState.Success(ShowDetails.PreviewSample),
            ratings = UiState.Success(Ratings.PreviewSample),
            seasons = UiState.Success(Season.PreviewSample),
            cast = UiState.Success(Cast.PreviewSample),
            review = UiState.Success(Review.PreviewSample.take(2)),
            relatedShows = UiState.Success(Show.PreviewSample),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun LoadingPreview() {
    SnacTheme {
        ShowScreen(
            onBackClick = {},
            onSeasonClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            show = UiState.Loading,
            ratings = UiState.Loading,
            seasons = UiState.Loading,
            cast = UiState.Loading,
            review = UiState.Loading,
            relatedShows = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        ShowScreen(
            onBackClick = {},
            onSeasonClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            show = error,
            ratings = error,
            seasons = error,
            cast = error,
            review = error,
            relatedShows = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
