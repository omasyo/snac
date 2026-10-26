package com.omasyo.snac.core.data.show

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toAnticipatedItem
import com.omasyo.snac.core.data.mappers.toCollectedItem
import com.omasyo.snac.core.data.mappers.toFavoritedItem
import com.omasyo.snac.core.data.mappers.toPeriod
import com.omasyo.snac.core.data.mappers.toPlayedItem
import com.omasyo.snac.core.data.mappers.toPopularItem
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.data.mappers.toWatchedItem
import com.omasyo.snac.core.data.paging.ChartPagingSource
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.show.ShowNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow


@ContributesBinding(AppScope::class)
@Inject
class ShowRepositoryImpl(
    private val showNetworkSource: ShowNetworkSource,
) : ShowRepository {
    override fun getAnticipated(): Flow<PagingData<ChartItem>> =
        Pager(initialKey = null, config = pagingConfig) {
            ChartPagingSource(
                provider = { page -> showNetworkSource.getAnticipated(page, PAGE_SIZE) },
                mapper = { it.toAnticipatedItem() },
            )
        }.flow

    override fun getMostCollected(period: TimePeriod): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page ->
                    showNetworkSource.getMostCollected(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toCollectedItem() },
            )
        }.flow

    override fun getMostFavorited(period: TimePeriod): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page ->
                    showNetworkSource.getMostFavorited(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toFavoritedItem() },
            )
        }.flow

    override fun getMostPlayed(period: TimePeriod): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page ->
                    showNetworkSource.getMostPlayed(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toPlayedItem() },
            )
        }.flow

    override fun getMostWatched(period: TimePeriod): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page ->
                    showNetworkSource.getMostCollected(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toWatchedItem() },
            )
        }.flow

    override fun getPopular(): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page -> showNetworkSource.getPopular(page, PAGE_SIZE) },
                mapper = { it.toPopularItem() },
            )
        }.flow

    override suspend fun getTopTrending(): SnacResult<List<TrendingItem>> =
        showNetworkSource.getTrending(1, 10).map { list -> list.map { it.toTrendingItem() } }

    override fun getTrending(): Flow<PagingData<ChartItem>> =
        Pager(config = pagingConfig) {
            ChartPagingSource(
                provider = { page -> showNetworkSource.getTrending(page, PAGE_SIZE) },
                mapper = { it.toTrendingItem() },
            )
        }.flow

    companion object {
        private const val PAGE_SIZE = 30

        private val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE, enablePlaceholders = false
        )
    }
}
