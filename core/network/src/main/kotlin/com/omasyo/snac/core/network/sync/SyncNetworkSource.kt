package com.omasyo.snac.core.network.sync

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.sync.models.PlaybackResponse
import com.omasyo.snac.core.network.sync.models.UpNextResponse

interface SyncNetworkSource : NetworkSource {
    suspend fun getUpNext(page: Int, limit: Int): SnacResult<List<UpNextResponse>>

    suspend fun getPlayback(page: Int, limit: Int): SnacResult<List<PlaybackResponse>>
}
