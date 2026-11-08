package com.omasyo.snac.core.data.show

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Review
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.model.extra.CommentSort
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingItem
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>>

    suspend fun getDetails(slug: String): SnacResult<ShowDetails>

    suspend fun getRatings(slug: String): SnacResult<Ratings>

    suspend fun getCast(slug: String): SnacResult<List<Cast>>

    fun getRelated(slug: String): Flow<PagingData<Show>>

    fun getComments(slug: String, sort: CommentSort): Flow<PagingData<Review>>

    suspend fun getTopComments(slug: String): SnacResult<List<Review>>

    fun getMostCollected(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostFavorited(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostPlayed(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostWatched(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>>


    suspend fun getTopTrending(): SnacResult<List<TrendingItem>>


    fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>>
}