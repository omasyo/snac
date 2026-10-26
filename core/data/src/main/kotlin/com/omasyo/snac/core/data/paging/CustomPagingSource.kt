package com.omasyo.snac.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.ChartItem

internal typealias ChartPagingSource<T> = CustomPagingSource<T, ChartItem>

internal open class CustomPagingSource<T, R : Any>(
    val provider: suspend (page: Int) -> SnacResult<List<T>>,
    val mapper: (T) -> R,
) : PagingSource<Int, R>() {
    override fun getRefreshKey(state: PagingState<Int, R>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
        val page = params.key ?: 1

        val result = when(val response = provider(page)) {
            is SnacResult.Failure -> return LoadResult.Error(Exception(response.error.toString()))
            is SnacResult.Success -> response.value
        }


        return LoadResult.Page(
            data = result.map(mapper),
            prevKey = if (page == 1) null else page.minus(1),
            nextKey = if (result.isEmpty()) null else page.plus(1),
        )
    }
}