package com.omasyo.snac.core.data.movie

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.model.extra.CommentSort
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.TrendingItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>>

    suspend fun getBoxOffice(): SnacResult<List<BoxOfficeItem>>

    fun getBoxOfficePaged(): Flow<PagingData<ChartItem>>

    suspend fun getDetails(slug: String): SnacResult<MovieDetails>

    suspend fun getRatings(slug: String): SnacResult<Ratings>

    suspend fun getCast(slug: String): SnacResult<List<Cast>>

    suspend fun getPeople(slug: String): SnacResult<PeopleList>

    fun getRelated(slug: String): Flow<PagingData<Movie>>

    fun getComments(slug: String, sort: CommentSort): Flow<PagingData<Comment>>

    suspend fun getTopComments(slug: String): SnacResult<List<Comment>>

    fun getMostCollected(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostFavorited(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostPlayed(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getMostWatched(period: TimePeriod, startPosition: Int): Flow<PagingData<ChartItem>>

    fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>>


    suspend fun getTopTrending(): SnacResult<List<TrendingItem>>


    fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>>
}