package com.omasyo.snac.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omasyo.snac.core.common.SnacException
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.ChartItem

internal typealias ChartPagingSource<T> = CustomPagingSource<T, ChartItem>

internal open class CustomPagingSource<T, R : Any>(
    val pageSize: Int,
    val provider: suspend (page: Int) -> SnacResult<List<T>>,
    val mapper: (T) -> R,
) : PagingSource<Int, R>() {
    override val jumpingSupported: Boolean get() = true

    override fun getRefreshKey(state: PagingState<Int, R>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
        val page = pageForPosition(params.key ?: 0)

        val result = when(val response = provider(page)) {
            is SnacResult.Failure -> return LoadResult.Error(SnacException(response.error))
            is SnacResult.Success -> response.value
        }

        if (params is LoadParams.Refresh && page > 1 && result.isEmpty()) {
            return load(LoadParams.Refresh(0, params.loadSize, params.placeholdersEnabled))
        }

        val firstPosition = (page - 1) * pageSize
        return LoadResult.Page(
            data = result.map(mapper),
            prevKey = if (page == 1) null else firstPosition - pageSize,
            nextKey = if (result.isEmpty()) null else firstPosition + pageSize,
            itemsBefore = firstPosition,
            itemsAfter = if (result.isEmpty()) 0 else LoadResult.Page.COUNT_UNDEFINED,
        )
    }

    private fun pageForPosition(position: Int): Int = (position.coerceAtLeast(0) / pageSize) + 1
}
