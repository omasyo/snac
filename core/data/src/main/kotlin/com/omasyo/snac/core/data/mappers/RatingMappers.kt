package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Rating
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.network.models.RatingsResponse
import java.text.DecimalFormat
import java.text.NumberFormat

fun RatingsResponse.toRatings() = Ratings(
    traktRating = trakt.rating,
    voteCount = trakt.votes,
    distribution = trakt.distribution,
    externalRatings = listOfNotNull(
        tmdb?.toRating(title = "TMDB", outOf = "10", pattern = "#.#"),
        imdb?.toRating(title = "IMDb", outOf = "10", pattern = "#.#"),
        rottenTomatoes?.let { rt ->
            rt.rating?.let { Rating(title = "Tomatometer", rating = "$it%", extra = rt.state.orEmpty().toTitleCase()) }
        },
        rottenTomatoes?.let { rt ->
            rt.userRating?.let { Rating(title = "Popcornometer", rating = "$it%", extra = rt.userState.orEmpty().toTitleCase()) }
        },
        metascore?.rating?.let { Rating(title = "Metascore", rating = it.toString(), extra = "Critic reviews", outOf = "100") },
        letterboxd?.toRating(title = "Letterboxd", outOf = "5", pattern = "#.##"),
        mal?.toRating(title = "MAL", outOf = "10", pattern = "#.##"),
    ),
)

private fun RatingsResponse.Scored.toRating(title: String, outOf: String, pattern: String): Rating? {
    val rating = rating ?: return null
    return Rating(
        title = title,
        rating = DecimalFormat(pattern).format(rating),
        extra = votes?.let { "${NumberFormat.getIntegerInstance().format(it)} ${if (it == 1) "vote" else "votes"}" }.orEmpty(),
        outOf = outOf,
    )
}

private fun String.toTitleCase(): String =
    split(' ').joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
