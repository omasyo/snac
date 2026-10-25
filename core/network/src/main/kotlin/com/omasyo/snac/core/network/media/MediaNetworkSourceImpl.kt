package com.omasyo.snac.core.network.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.media.models.TrendingItemResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class MediaNetworkSourceImpl(
    private val client: HttpClient
) : MediaNetworkSource {
    override suspend fun getTopTrendingMedia(): SnacResult<List<TrendingItemResponse>> =
        safeCall {
            client.get("media/trending") {
                parameter("limit", 1)
            }
        }
}