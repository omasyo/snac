package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.TrendingShowResponse

interface ShowNetworkSource : NetworkSource {
    suspend fun getTrending(page: Int, limit: Int): SnacResult<List<TrendingShowResponse>>
}