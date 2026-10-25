package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.media.models.ShowItemResponse
import com.omasyo.snac.core.network.safeCall
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
    override suspend fun getTrendingMedia(page: Int, limit: Int): SnacResult<List<ShowItemResponse>> =
        safeCall {
            client.get("shows/trending") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }
}