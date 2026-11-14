package com.omasyo.snac.core.data.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.data.mappers.toTrendingSearchItem
import com.omasyo.snac.core.data.paging.CustomPagingSource
import com.omasyo.snac.core.model.media.TrendingSearchItem
import com.omasyo.snac.core.network.search.SearchNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
@Inject
class SearchRepositoryImpl(
    private val searchNetworkSource: SearchNetworkSource,
) : SearchRepository {
    override fun getTrendingSearchResults(): Flow<PagingData<TrendingSearchItem>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> searchNetworkSource.getTrendingSearchResults(page, PAGE_SIZE) },
                mapper = { it.toTrendingSearchItem() },
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
