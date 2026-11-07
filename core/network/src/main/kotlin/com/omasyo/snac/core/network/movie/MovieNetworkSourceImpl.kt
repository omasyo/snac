package com.omasyo.snac.core.network.movie

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.media.models.AnticipatedMovieResponse
import com.omasyo.snac.core.network.media.models.TrendingMovieResponse
import com.omasyo.snac.core.network.models.CommentResponse
import com.omasyo.snac.core.network.models.CommentSort
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.models.RatingsResponse
import com.omasyo.snac.core.network.movie.models.BoxOfficeResponse
import com.omasyo.snac.core.network.movie.models.MovieDetailsResponse
import com.omasyo.snac.core.network.movie.models.MostFavoritedMovieResponse
import com.omasyo.snac.core.network.movie.models.MostMovieStatsResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class MovieNetworkSourceImpl(
    private val client: HttpClient
) : MovieNetworkSource {
    override suspend fun getAnticipated(
        page: Int,
        limit: Int
    ): SnacResult<List<AnticipatedMovieResponse>> = safeCall {
        client.get("movies/anticipated") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getBoxOffice(): SnacResult<List<BoxOfficeResponse>> =
        safeCall { client.get("movies/boxoffice") }

    override suspend fun getDetails(slug: String): SnacResult<MovieDetailsResponse> = safeCall {
        client.get("movies/$slug") {
            parameter("extended", "full")
        }
    }

    override suspend fun getRatings(slug: String): SnacResult<RatingsResponse> = safeCall {
        client.get("movies/$slug/ratings") {
            parameter("extended", "all")
        }
    }

    override suspend fun getComments(
        slug: String,
        sort: CommentSort,
        page: Int,
        limit: Int,
    ): SnacResult<List<CommentResponse>> = safeCall {
        client.get("movies/$slug/comments/${sort.slug}") {
            parameter("extended", "full")
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostCollected(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostMovieStatsResponse>> = safeCall {
        client.get("movies/collected/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostFavorited(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostFavoritedMovieResponse>> = safeCall {
        client.get("movies/favorited/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostPlayed(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostMovieStatsResponse>> = safeCall {
        client.get("movies/played/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostWatched(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostMovieStatsResponse>> = safeCall {
        client.get("movies/watched/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getPopular(
        page: Int,
        limit: Int
    ): SnacResult<List<MovieResponse>> = safeCall {
        client.get("movies/popular") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getTrending(
        page: Int,
        limit: Int
    ): SnacResult<List<TrendingMovieResponse>> =
        safeCall {
            client.get("movies/trending") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }
}