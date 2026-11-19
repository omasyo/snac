package com.omasyo.snac.core.network.sync

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.sync.models.PlaybackResponse
import com.omasyo.snac.core.network.sync.models.UpNextResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class SyncNetworkSourceImpl(
    private val client: HttpClient
) : SyncNetworkSource {
    override suspend fun getUpNext(
        page: Int,
        limit: Int,
    ): SnacResult<List<UpNextResponse>> = safeCall {
        client.get("sync/progress/up_next") {
            parameter("extended", "full")
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getPlayback(
        page: Int,
        limit: Int,
    ): SnacResult<List<PlaybackResponse>> = safeCall {
        client.get("sync/playback") {
            parameter("extended", "full")
            parameter("page", page)
            parameter("limit", limit)
        }
    }
}
