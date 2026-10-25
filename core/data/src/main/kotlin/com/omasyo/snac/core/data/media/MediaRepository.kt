package com.omasyo.snac.core.data.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.TrendingItem

interface MediaRepository {
    suspend fun getTopTrending(): SnacResult<TrendingItem?>

    suspend fun getAnticipated() : SnacResult<List<AnticipatedItem>>
}