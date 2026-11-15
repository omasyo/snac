package com.omasyo.snac.core.data.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.data.mappers.toMovie
import com.omasyo.snac.core.data.mappers.toPerson
import com.omasyo.snac.core.data.mappers.toShow
import com.omasyo.snac.core.data.mappers.toTrendingSearchItem
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.paging.CustomPagingSource
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
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
                provider = { page ->
                    searchNetworkSource.getTrendingSearchResults(
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toTrendingSearchItem() },
            )
        }.flow

    override fun searchMovies(query: String): Flow<PagingData<Movie>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> searchNetworkSource.searchMovies(query, page, PAGE_SIZE) },
                mapper = { it.movie.toMovie() },
            )
        }.flow

    override fun searchShows(query: String): Flow<PagingData<Show>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> searchNetworkSource.searchShows(query, page, PAGE_SIZE) },
                mapper = { it.show.toShow() },
            )
        }.flow

    override fun searchPeople(query: String): Flow<PagingData<Person>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> searchNetworkSource.searchPeople(query, page, PAGE_SIZE) },
                mapper = { it.person.toPerson() },
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
