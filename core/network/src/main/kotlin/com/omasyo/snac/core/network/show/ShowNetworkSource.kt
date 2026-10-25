package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.ShowItemResponse

interface ShowNetworkSource : NetworkSource {
    suspend fun getTrendingMedia(page: Int, limit: Int): SnacResult<List<ShowItemResponse>>
}