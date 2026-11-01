package com.omasyo.snac.feature.media.impl.movie

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
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.ReleaseStatus
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
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
import com.omasyo.snac.feature.media.impl.common.MediaGenres
import com.omasyo.snac.feature.media.impl.common.MediaHero
import com.omasyo.snac.feature.media.impl.common.MediaHeroContent
import com.omasyo.snac.feature.media.impl.common.MediaInfo
import com.omasyo.snac.feature.media.impl.common.MediaOverview
import com.omasyo.snac.feature.media.impl.common.MediaTagline
import com.omasyo.snac.feature.media.impl.common.RelatedSection
import com.omasyo.snac.feature.media.impl.common.ReviewSection
import com.omasyo.snac.feature.media.impl.components.RatingsSection
import com.omasyo.snac.feature.media.impl.components.Review

@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onReviewClick: (Review) -> Unit,
    onSeeAllReviewClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSeeAllRelatedClick: () -> Unit,
    movie: UiState<MovieDetails>,
    ratings: UiState<Ratings>,
    cast: UiState<List<Cast>>,
    review: UiState<List<Review>>,
    relatedMovies: UiState<List<Movie>>,
    modifier: Modifier = Modifier,
) {
    MediaDetailsLayout(onBackClick = onBackClick, modifier = modifier) {
        MediaHero(
            state = movie.map {
                MediaHeroContent(
                    title = it.title,
                    posterUrl = it.posterUrl,
                    secondaryPosterUrl = it.secondaryPosterUrl,
                )
            },
        )
        movie.map { it.tagline }.transpose()?.let {
            MediaTagline(
                state = it,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        MediaInfo(
            state = movie.map { it.info() },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        MediaGenres(
            state = movie.map { it.genres },
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        MediaOverview(
            state = movie.map { it.overview },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        SignInPrompt(
            title = "Track this film",
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
        )
        RelatedSection(
            onMediaClick = onMediaClick,
            onSeeAllClick = onSeeAllRelatedClick,
            state = relatedMovies,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun MovieDetails.info(): List<Pair<String, String>> = listOf(
    stringResource(R.string.info_year) to year.toString(),
    stringResource(R.string.info_runtime) to runtime.formatRuntime(),
    stringResource(R.string.info_cert) to certification,
    stringResource(R.string.info_status) to status.label(),
    stringResource(R.string.info_released) to releaseDate.formatDayMonth(),
    stringResource(R.string.info_country) to country.uppercase(),
    stringResource(R.string.info_language) to language.uppercase(),
//    "Trakt" to rating.formatRating(),
)

@Composable
private fun ReleaseStatus.label(): String = when (this) {
    ReleaseStatus.Released -> stringResource(R.string.release_status_released)
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        MovieScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            movie = UiState.Success(MovieDetails.PreviewSample),
            ratings = UiState.Success(Ratings.PreviewSample),
            cast = UiState.Success(Cast.PreviewSample),
            review = UiState.Success(Review.PreviewSample.take(2)),
            relatedMovies = UiState.Success(Movie.PreviewSample),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun LoadingPreview() {
    SnacTheme {
        MovieScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            movie = UiState.Loading,
            ratings = UiState.Loading,
            cast = UiState.Loading,
            review = UiState.Loading,
            relatedMovies = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        MovieScreen(
            onBackClick = {},
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
            movie = error,
            ratings = error,
            cast = error,
            review = error,
            relatedMovies = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
