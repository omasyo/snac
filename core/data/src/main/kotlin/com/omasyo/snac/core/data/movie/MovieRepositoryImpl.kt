package com.omasyo.snac.core.data.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toAnticipatedItem
import com.omasyo.snac.core.data.mappers.toBoxOfficeItem
import com.omasyo.snac.core.data.mappers.toCollectedItem
import com.omasyo.snac.core.data.mappers.toFavoritedItem
import com.omasyo.snac.core.data.mappers.toPeriod
import com.omasyo.snac.core.data.mappers.toPlayedItem
import com.omasyo.snac.core.data.mappers.toPopularItem
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.data.mappers.toMovieDetails
import com.omasyo.snac.core.data.mappers.toRatings
import com.omasyo.snac.core.data.mappers.toWatchedItem
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.paging.ChartPagingSource
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.movie.MovieNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow


@ContributesBinding(AppScope::class)
@Inject
class MovieRepositoryImpl(
    private val movieNetworkSource: MovieNetworkSource,
) : MovieRepository {
    override fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> movieNetworkSource.getAnticipated(page, PAGE_SIZE) },
                mapper = { it.toAnticipatedItem() },
            )
        }.flow

    override suspend fun getBoxOffice(): SnacResult<List<BoxOfficeItem>> =
        movieNetworkSource.getBoxOffice().map { list -> list.map { it.toBoxOfficeItem() } }

    override fun getBoxOfficePaged(): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    if (page == 1) movieNetworkSource.getBoxOffice()
                    else SnacResult.Success(emptyList())
                },
                mapper = { it.toBoxOfficeItem() },
            )
        }.flow

    override suspend fun getDetails(slug: String): SnacResult<MovieDetails> =
        movieNetworkSource.getDetails(slug).map { it.toMovieDetails() }

    override suspend fun getRatings(slug: String): SnacResult<Ratings> =
        movieNetworkSource.getRatings(slug).map { it.toRatings() }

    override fun getMostCollected(
        period: TimePeriod,
        startPosition: Int,
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    movieNetworkSource.getMostCollected(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toCollectedItem() },
            )
        }.flow

    override fun getMostFavorited(
        period: TimePeriod,
        startPosition: Int,
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    movieNetworkSource.getMostFavorited(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toFavoritedItem() },
            )
        }.flow

    override fun getMostPlayed(
        period: TimePeriod,
        startPosition: Int,
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    movieNetworkSource.getMostPlayed(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toPlayedItem() },
            )
        }.flow

    override fun getMostWatched(
        period: TimePeriod,
        startPosition: Int,
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    movieNetworkSource.getMostWatched(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toWatchedItem() },
            )
        }.flow

    override fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> movieNetworkSource.getPopular(page, PAGE_SIZE) },
                mapper = { it.toPopularItem() },
            )
        }.flow

    override suspend fun getTopTrending(): SnacResult<List<TrendingItem>> =
        movieNetworkSource.getTrending(1, 10).map { list -> list.map { it.toTrendingItem() } }

    override fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> movieNetworkSource.getTrending(page, PAGE_SIZE) },
                mapper = { it.toTrendingItem() },
            )
        }.flow

    companion object {
        private const val PAGE_SIZE = 30

        private val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            jumpThreshold = PAGE_SIZE * 3,
        )
    }
}