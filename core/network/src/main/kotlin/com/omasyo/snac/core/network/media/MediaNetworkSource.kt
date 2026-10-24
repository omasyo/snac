package com.omasyo.snac.core.network.media

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.TrendingItem

interface MediaNetworkSource : NetworkSource {
    suspend fun getTopTrendingMedia(): SnacResult<List<TrendingItem>>
}