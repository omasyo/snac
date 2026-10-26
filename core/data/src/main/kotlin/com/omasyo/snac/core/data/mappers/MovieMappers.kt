package com.omasyo.snac.core.data.mappers

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