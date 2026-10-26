package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.media.models.TrendingShowResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse
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