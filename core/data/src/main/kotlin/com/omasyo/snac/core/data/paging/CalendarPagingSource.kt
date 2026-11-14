package com.omasyo.snac.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.mappers.toCalendarItem
import com.omasyo.snac.core.model.media.CalendarItem
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import java.time.LocalDate

internal class CalendarPagingSource(
    private val windowDays: Int,
    private val provider: suspend (startDate: LocalDate, days: Int) -> SnacResult<List<CalendarMediaResponse>>,
) : PagingSource<LocalDate, CalendarItem>() {

    override fun getRefreshKey(state: PagingState<LocalDate, CalendarItem>): LocalDate? =
        state.anchorPosition?.let { state.closestItemToPosition(it)?.date }

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, CalendarItem> {
        val start = params.key ?: LocalDate.now()

        val response = when (val result = provider(start, windowDays)) {
            is SnacResult.Failure -> return LoadResult.Error(Exception(result.error.toString()))
            is SnacResult.Success -> result.value
        }

        return LoadResult.Page(
            data = response.mapNotNull { it.toCalendarItem() }.sortedBy { it.date },
            prevKey = start.minusDays(windowDays.toLong()),
            nextKey = start.plusDays(windowDays.toLong()),
        )
    }
}
