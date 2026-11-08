package com.omasyo.snac.core.data.show

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>>

    suspend fun getDetails(slug: String): SnacResult<ShowDetails>

    fun getMostCollected(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostFavorited(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostPlayed(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostWatched(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>>


    suspend fun getTopTrending(): SnacResult<List<TrendingItem>>


    fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>>
}