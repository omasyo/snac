package com.omasyo.snac.core.network.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.TrendingItemResponse

interface MediaNetworkSource : NetworkSource {
    suspend fun getTopTrendingMedia(): SnacResult<List<TrendingItemResponse>>
}