package com.omasyo.snac.core.data.search

import androidx.paging.PagingData
import com.omasyo.snac.core.model.media.TrendingSearchItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getTrendingSearchResults(): Flow<PagingData<TrendingSearchItem>>
}
