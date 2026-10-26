package com.omasyo.snac.core.data.movie

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAnticipated(): Flow<PagingData<ChartItem>>

    suspend fun getBoxOffice(): SnacResult<List<BoxOfficeItem>>

    fun getMostCollected(period: TimePeriod): Flow<PagingData<ChartItem>>

    fun getMostFavorited(period: TimePeriod): Flow<PagingData<ChartItem>>

    fun getMostPlayed(period: TimePeriod): Flow<PagingData<ChartItem>>

    fun getMostWatched(period: TimePeriod): Flow<PagingData<ChartItem>>

    fun getPopular(): Flow<PagingData<ChartItem>>


    suspend fun getTopTrending(): SnacResult<List<TrendingItem>>


    fun getTrending(): Flow<PagingData<ChartItem>>
}