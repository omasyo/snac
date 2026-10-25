package com.omasyo.snac.core.data.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.TrendingItem

interface MediaRepository {
    suspend fun getTopTrendingMedia(): SnacResult<TrendingItem>
}