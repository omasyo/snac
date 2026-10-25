package com.omasyo.snac.core.data.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.TrendingItem

interface ShowRepository {
    suspend fun getTrendingMedia(): SnacResult<List<TrendingItem>>
}