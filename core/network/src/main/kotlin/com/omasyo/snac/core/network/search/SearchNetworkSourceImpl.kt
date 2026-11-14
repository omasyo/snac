package com.omasyo.snac.core.network.search

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.search.models.TrendingSearchResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class SearchNetworkSourceImpl(
    private val client: HttpClient
) : SearchNetworkSource {
    override suspend fun getTrendingSearchResults(
        page: Int,
        limit: Int
    ): SnacResult<List<TrendingSearchResponse>> =
        safeCall {
            client.get("search/recent_by_id/global/movies,shows") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }
}
