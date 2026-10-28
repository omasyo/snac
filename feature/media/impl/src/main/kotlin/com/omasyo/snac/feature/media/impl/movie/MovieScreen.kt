package com.omasyo.snac.feature.media.impl.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class Genre(val name: String)

enum class ReleaseStatus { Released }

data class SocialIds(val instagram: String?)

data class Rating(
    val title: String,
    val rating: String,
    val extra: String,
    val outOf: String? = null,
)

data class Ratings(
    val traktRating: Double,
    val distribution: Map<String, Int>,
    val externalRatings: List<Rating>,
) {
    companion object {
        val PreviewSample = Ratings(
            traktRating = 8.56438,
            distribution = mapOf(
                "1" to 89,
                "2" to 83,
                "3" to 59,
                "4" to 262,
                "5" to 570,
                "6" to 2444,
                "7" to 4187,
                "8" to 10738,
                "9" to 6179,
                "10" to 14600,
            ),
            externalRatings = listOf(
                Rating(
                    title = "TMDB",
                    rating = "8.5",
                    extra = "6,209 votes",
                    outOf = "10",
                ),
                Rating(
                    title = "IMDb",
                    rating = "7.9",
                    extra = "366,130 votes",
                    outOf = "10",
                ),
                Rating(
                    title = "Metascore",
                    rating = "66",
                    extra = "Critic reviews",
                    outOf = "100",
                ),
                Rating(
                    title = "Tomatometer",
                    rating = "90%",
                    extra = "Fresh",
                ),
                Rating(
                    title = "Popcornometer",
                    rating = "97%",
                    extra = "Upright",
                ),
                Rating(
                    title = "Letterboxd",
                    rating = "4.03",
                    extra = "4,621,098 votes",
                    outOf = "5",
                ),
            ),
        )
    }
}

// Checkout videos
// Comments extended or not makes no difference

data class MovieDetails(
    val slug: String,
    val title: String,
    val year: Int,
    val imageUrl: String?,
    val genres: List<Genre>,
    val rating: Double,
    val status: ReleaseStatus,
    val runtime: Duration,
    val tagline: String?,
    val homepage: String?,
    val trailer: String?,
    val country: String,
    val language: String,
    val overview: String,
    val releaseDate: LocalDate,
    val socialIds: SocialIds,
    val certification: String,
    val commentCount: Int,
    val originalTitle: String,
) {
    companion object {
        val PreviewSample = MovieDetails(
            slug = "slug",
            title = "Frankenstein",
            year = 2025,
            imageUrl = null,
            genres = listOf(Genre("Drama"), Genre("Horror")),
            rating = 7.5,
            status = ReleaseStatus.Released,
            runtime = 90.minutes,
            tagline = "Only monsters play God.",
            homepage = "www.website.com",
            trailer = "www.youtube.com",
            country = "us",
            language = "en",
            overview = "Dr. Victor Frankenstein, a brilliant but egotistical scientist, brings a creature to life in a monstrous experiment that ultimately leads to the undoing of both the creator and his tragic creation.",
            releaseDate = LocalDate.now(),
            socialIds = SocialIds(instagram = null),
            certification = "PG-13",
            commentCount = 123,
            originalTitle = "Frankenstein"
        )
    }
}

//data class Comment()

@Composable
fun MovieScreen(
    movie: MovieDetails,
    ratings: Ratings,
    cast: List<Any>,
    relatedMovies: List<Movie>,
    modifier: Modifier = Modifier,
) {
    Column(modifier.background(SnacColors.page)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(13f / 18f)
                .film()
        ) {
            Image(
                imageUrl = movie.imageUrl,
                modifier = Modifier.matchParentSize(),
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .align(Alignment.BottomStart),
            ) {
//                Image(
//                    imageUrl = movie.imageUrl,
//                    modifier = Modifier
//                        .silhouette(SnacColors.signal)
//                        .border(4.dp, SnacColors.chip)
//                        .size(112.dp, 168.dp),
//                )
                Text(
                    text = "The Amazing Spiderman 2",
                    style = SnacTypography.display.copy(
//                        lineHeightStyle = CapHeightLineHeightStyle,
                    ),
                    color = SnacColors.onFilm,
//                    overflow = TextOverflow.
//                    autoSize = TextAutoSize.StepBased(),
                    modifier = Modifier.silhouette(SnacColors.signal)

                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        MovieScreen(
            movie = MovieDetails.PreviewSample,
            ratings = Ratings.PreviewSample,
            cast = emptyList(),
            relatedMovies = Movie.PreviewSample,
            modifier = Modifier.fillMaxSize(),
        )
    }
}