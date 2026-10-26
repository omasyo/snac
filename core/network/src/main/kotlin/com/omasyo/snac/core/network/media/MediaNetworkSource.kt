package com.omasyo.snac.core.network.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.AnticipatedMediaResponse
import com.omasyo.snac.core.network.media.models.TrendingMediaResponse

interface MediaNetworkSource : NetworkSource {
    suspend fun getAnticipated(page: Int, limit: Int): SnacResult<List<AnticipatedMediaResponse>>

    suspend fun getTopTrending(): SnacResult<List<TrendingMediaResponse>>
}