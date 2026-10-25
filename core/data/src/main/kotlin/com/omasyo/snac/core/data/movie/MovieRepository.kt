package com.omasyo.snac.core.data.movie

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.TrendingItem

interface MovieRepository {
    suspend fun getBoxOffice() : SnacResult<List<BoxOfficeItem>>

    suspend fun getTrending(): SnacResult<List<TrendingItem>>
}