package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.media.models.TrendingShowResponse
import com.omasyo.snac.core.network.models.CommentResponse
import com.omasyo.snac.core.network.models.CommentSort
import com.omasyo.snac.core.network.models.PeopleResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.models.RatingsResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse
import com.omasyo.snac.core.network.show.models.SeasonResponse
import com.omasyo.snac.core.network.show.models.ShowDetailsResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class ShowNetworkSourceImpl(
    private val client: HttpClient
) : ShowNetworkSource {
    override suspend fun getAnticipated(
        page: Int,
        limit: Int
    ): SnacResult<List<AnticipatedShowResponse>> = safeCall {
        client.get("shows/anticipated") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getDetails(slug: String): SnacResult<ShowDetailsResponse> = safeCall {
        client.get("shows/$slug") {
            parameter("extended", "full")
        }
    }

    override suspend fun getSeasons(slug: String): SnacResult<List<SeasonResponse>> = safeCall {
        client.get("shows/$slug/seasons") {
            parameter("extended", "full")
        }
    }

    override suspend fun getSeason(slug: String, season: Int): SnacResult<SeasonResponse> =
        safeCall {
            client.get("shows/$slug/seasons/$season/info") {
                parameter("extended", "full")
            }
        }

    override suspend fun getRatings(slug: String): SnacResult<RatingsResponse> = safeCall {
        client.get("shows/$slug/ratings") {
            parameter("extended", "all")
        }
    }

    override suspend fun getPeople(slug: String): SnacResult<PeopleResponse> = safeCall {
        client.get("shows/$slug/people") {
            parameter("extended", "full")
        }
    }

    override suspend fun getRelated(
        slug: String,
        page: Int,
        limit: Int,
    ): SnacResult<List<ShowResponse>> = safeCall {
        client.get("shows/$slug/related") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getComments(
        slug: String,
        sort: CommentSort,
        page: Int,
        limit: Int,
    ): SnacResult<List<CommentResponse>> = safeCall {
        client.get("shows/$slug/comments/${sort.slug}") {
            parameter("extended", "full")
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostCollected(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostShowStatsResponse>> = safeCall {
        client.get("shows/collected/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostFavorited(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostFavoritedShowResponse>> = safeCall {
        client.get("shows/favorited/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostPlayed(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostShowStatsResponse>> = safeCall {
        client.get("shows/played/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getMostWatched(
        period: Period,
        page: Int,
        limit: Int
    ): SnacResult<List<MostShowStatsResponse>> = safeCall {
        client.get("shows/watched/${period.slug}") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getPopular(
        page: Int,
        limit: Int
    ): SnacResult<List<ShowResponse>> = safeCall {
        client.get("shows/popular") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getTrending(
        page: Int,
        limit: Int
    ): SnacResult<List<TrendingShowResponse>> =
        safeCall {
            client.get("shows/trending") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }
}