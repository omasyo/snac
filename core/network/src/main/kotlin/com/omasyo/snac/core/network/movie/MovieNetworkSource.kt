package com.omasyo.snac.core.network.movie

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.AnticipatedMovieResponse
import com.omasyo.snac.core.network.media.models.TrendingMovieResponse
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.movie.models.BoxOfficeResponse
import com.omasyo.snac.core.network.movie.models.MostFavoritedMovieResponse
import com.omasyo.snac.core.network.movie.models.MostMovieStatsResponse

interface MovieNetworkSource : NetworkSource {
    suspend fun getAnticipated(page: Int, limit: Int): SnacResult<List<AnticipatedMovieResponse>>

    suspend fun getBoxOffice(): SnacResult<List<BoxOfficeResponse>>

    suspend fun getMostCollected(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostMovieStatsResponse>>

    suspend fun getMostFavorited(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostFavoritedMovieResponse>>

    suspend fun getMostPlayed(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostMovieStatsResponse>>

    suspend fun getMostWatched(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostMovieStatsResponse>>

    suspend fun getPopular(page: Int, limit: Int): SnacResult<List<MovieResponse>>

    suspend fun getTrending(page: Int, limit: Int): SnacResult<List<TrendingMovieResponse>>
}