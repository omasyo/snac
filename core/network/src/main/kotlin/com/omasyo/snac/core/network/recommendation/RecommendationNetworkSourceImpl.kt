package com.omasyo.snac.core.network.recommendation

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class RecommendationNetworkSourceImpl(
    private val client: HttpClient
) : RecommendationNetworkSource {
    override suspend fun getMovies(limit: Int): SnacResult<List<MovieResponse>> = safeCall {
        client.get("recommendations/movies") {
            parameter("extended", "full")
            parameter("limit", limit)
        }
    }

    override suspend fun getShows(limit: Int): SnacResult<List<ShowResponse>> = safeCall {
        client.get("recommendations/shows") {
            parameter("extended", "full")
            parameter("limit", limit)
        }
    }
}
