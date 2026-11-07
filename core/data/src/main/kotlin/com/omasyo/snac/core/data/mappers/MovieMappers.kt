package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.ReleaseStatus
import com.omasyo.snac.core.data.models.SocialIds
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.CollectedItem
import com.omasyo.snac.core.model.media.FavoritedItem
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.PlayedItem
import com.omasyo.snac.core.model.media.PopularItem
import com.omasyo.snac.core.model.media.WatchedItem
import com.omasyo.snac.core.network.media.models.AnticipatedMovieResponse
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.movie.models.BoxOfficeResponse
import com.omasyo.snac.core.network.movie.models.MostFavoritedMovieResponse
import com.omasyo.snac.core.network.movie.models.MostMovieStatsResponse
import com.omasyo.snac.core.network.movie.models.MovieDetailsResponse
import kotlin.time.Duration.Companion.minutes

fun MovieResponse.toMovie() = Movie(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)

fun MovieResponse.toPopularItem() = PopularItem(
    media = toMovie()
)

fun AnticipatedMovieResponse.toAnticipatedItem() = AnticipatedItem(
    media = movie.toMovie(),
    listCount = listCount,
)

fun BoxOfficeResponse.toBoxOfficeItem() = BoxOfficeItem(
    media = movie.toMovie(),
    revenue = revenue,
)

fun MostMovieStatsResponse.toCollectedItem() = CollectedItem(
    media = movie.toMovie(),
    collectedCount = collectedCount,
    collectorCount = null,
)

fun MostFavoritedMovieResponse.toFavoritedItem() = FavoritedItem(
    media = movie.toMovie(),
    userCount = userCount,
)

fun MostMovieStatsResponse.toPlayedItem() = PlayedItem(
    media = movie.toMovie(),
    playCount = playCount,
)

fun MostMovieStatsResponse.toWatchedItem() = WatchedItem(
    media = movie.toMovie(),
    watcherCount = watcherCount,
)

fun MovieDetailsResponse.toMovieDetails() = MovieDetails(
    slug = ids.slug,
    title = title,
    year = year,
    posterUrl = images?.poster?.firstOrNull()?.toImageUrl(),
    secondaryPosterUrl = images?.fanart?.firstOrNull()?.toImageUrl()
        ?: images?.poster?.getOrNull(1)?.toImageUrl(),
    genres = genres.map { it.toGenre() },
    rating = rating,
    status = status?.toReleaseStatus(),
    runtime = runtime?.takeIf { it > 0 }?.minutes,
    tagline = tagline?.takeIf { it.isNotBlank() },
    homepage = homepage?.takeIf { it.isNotBlank() },
    trailer = trailer?.takeIf { it.isNotBlank() },
    country = country?.takeIf { it.isNotBlank() },
    language = language?.takeIf { it.isNotBlank() },
    overview = overview?.takeIf { it.isNotBlank() },
    releaseDate = released?.toLocalDateOrNull(),
    socialIds = SocialIds(instagram = socialIds?.instagram),
    certification = certification?.takeIf { it.isNotBlank() },
    commentCount = commentCount,
    originalTitle = originalTitle ?: title,
)

private fun String.toReleaseStatus(): ReleaseStatus? = when (this) {
    "released" -> ReleaseStatus.Released
    "in production" -> ReleaseStatus.InProduction
    "post production" -> ReleaseStatus.PostProduction
    "planned" -> ReleaseStatus.Planned
    "rumored" -> ReleaseStatus.Rumored
    "canceled" -> ReleaseStatus.Canceled
    else -> null
}