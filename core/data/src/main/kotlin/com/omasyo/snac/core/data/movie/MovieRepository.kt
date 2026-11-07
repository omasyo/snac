package com.omasyo.snac.core.data.movie

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>>

    suspend fun getBoxOffice(): SnacResult<List<BoxOfficeItem>>

    fun getBoxOfficePaged(): Flow<PagingData<ChartItem>>

    suspend fun getDetails(slug: String): SnacResult<MovieDetails>

    fun getMostCollected(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostFavorited(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostPlayed(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostWatched(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>>


    suspend fun getTopTrending(): SnacResult<List<TrendingItem>>


    fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>>
}