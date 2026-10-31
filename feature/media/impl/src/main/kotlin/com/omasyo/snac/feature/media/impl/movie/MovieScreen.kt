package com.omasyo.snac.feature.media.impl.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Genre
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.components.GenreChip
import com.omasyo.snac.feature.media.impl.components.GenreChipPlaceholder
import com.omasyo.snac.feature.media.impl.components.MediaInfoClapper
import com.omasyo.snac.feature.media.impl.components.MediaInfoClapperPlaceholder
import com.omasyo.snac.feature.media.impl.components.RatingsSection
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun MovieScreen(
    onPersonClick: (Person) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onSeeAllRelatedClick: () -> Unit,
    movie: UiState<MovieDetails>,
    ratings: UiState<Ratings>,
    cast: UiState<List<Cast>>,
    relatedMovies: UiState<List<Movie>>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(SnacColors.page)
            .navigationBarsPadding(),
    ) {
        Hero(state = movie)
        movie.map { it.tagline }.transpose()?.let {
            Tagline(
                state = it,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        Info(
            state = movie.map { it.info() },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Genres(
            state = movie.map { it.genres },
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Overview(
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
        RelatedSection(
            onMediaClick = onMediaClick,
            onSeeAllClick = onSeeAllRelatedClick,
            state = relatedMovies,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun Hero(
    state: UiState<MovieDetails>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(13f / 18f)
            .film()
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
                        .rotate(-4f)
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .align(Alignment.BottomStart),
                ) {
                    Box(
                        modifier = Modifier
                            .silhouette(SnacColors.signal)
                            .border(4.dp, SnacColors.chip)
                            .size(112.dp, 168.dp)
                            .background(imageGradient())
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            Modifier
                                .size(280.dp, 40.dp)
                                .background(SnacColors.faint)
                        )
                        Box(
                            Modifier
                                .size(200.dp, 40.dp)
                                .background(SnacColors.faint)
                        )
                    }
                }
            }

            is UiState.Success -> {
                val movie = state.content
                Image(
                    imageUrl = movie.secondaryPosterUrl ?: movie.posterUrl,
                    modifier = Modifier.matchParentSize(),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .rotate(if (movie.secondaryPosterUrl != null) -4f else 0f)
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .align(Alignment.BottomStart),
                ) {
                    if (movie.secondaryPosterUrl != null) {
                        Image(
                            imageUrl = movie.posterUrl,
                            modifier = Modifier
                                .silhouette(SnacColors.signal)
                                .border(4.dp, SnacColors.chip)
                                .size(112.dp, 168.dp),
                        )
                    }
                    Text(
                        text = movie.title,
                        style = SnacTypography.display.copy(
                            lineHeightStyle = CapHeightLineHeightStyle,
                            hyphens = Hyphens.Auto,
                        ),
                        color = SnacColors.onFilm,
                        modifier = Modifier
                            .weight(1f)
                            .silhouette(SnacColors.signal)
                    )
                }
            }
        }
    }
}

@Composable
private fun Tagline(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> {
            Text(
                text = state.content,
                style = SnacTypography.hand,
                color = SnacColors.signal,
                modifier = modifier,
            )
        }

        else -> {
            Box(
                modifier
                    .size(200.dp, 16.dp)
                    .background(SnacColors.faint)
            )
        }
    }
}

@Composable
private fun Info(
    state: UiState<List<Pair<String, String>>>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> MediaInfoClapper(info = state.content, modifier = modifier)
        else -> MediaInfoClapperPlaceholder(modifier = modifier)
    }
}

@Composable
private fun Genres(
    state: UiState<List<Genre>>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (state) {
            is UiState.Success -> state.content.forEach {
                val rotation = remember(it) { Random(it.hashCode()).nextInt(-3, 3).toFloat() }
                GenreChip(label = it.name, modifier = Modifier.rotate(rotation))
            }

            else -> repeat(3) {
                val rotation = remember(it) { Random(it + 80).nextInt(-3, 3).toFloat() }
                GenreChipPlaceholder(Modifier.rotate(rotation))
            }
        }
    }
}

@Composable
private fun Overview(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> {
            Text(
                text = state.content,
                style = SnacTypography.body,
                modifier = modifier,
            )
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier,
            ) {
                repeat(4) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(SnacColors.faint)
                    )
                }
                Box(
                    Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .background(SnacColors.faint)
                )
            }
        }
    }
}

private fun MovieDetails.info(): List<Pair<String, String>> = listOf(
    "Year" to year.toString(),
    "Runtime" to runtime.toComponents { hours, minutes, _, _ ->
        if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    },
    "Cert" to certification,
    "Status" to status.name,
    "Released" to releaseDate.format(DateTimeFormatter.ofPattern("d MMM")),
    "Country" to country.uppercase(),
    "Language" to language.uppercase(),
//    "Trakt" to DecimalFormat("#.#").format(rating),
)

@Preview(device = "spec:width=1080px,height=7000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        MovieScreen(
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onSeeAllRelatedClick = {},
            movie = UiState.Success(MovieDetails.PreviewSample),
            ratings = UiState.Success(Ratings.PreviewSample),
            cast = UiState.Success(Cast.PreviewSample),
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
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onSeeAllRelatedClick = {},
            movie = UiState.Loading,
            ratings = UiState.Loading,
            cast = UiState.Loading,
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
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = {},
            onSeeAllRelatedClick = {},
            movie = error,
            ratings = error,
            cast = error,
            relatedMovies = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
