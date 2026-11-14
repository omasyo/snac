package com.omasyo.snac.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omasyo.snac.core.common.SnacException
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.mappers.toCalendarItem
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import java.time.LocalDate

internal class CalendarPagingSource(
    private val windowDays: Int,
    private val maxEmptyWindowsAhead: Int = 8,
    private val provider: suspend (startDate: LocalDate, days: Int) -> SnacResult<List<CalendarMediaResponse>>,
) : PagingSource<LocalDate, CalendarDay>() {

    private var emptyWindowsAhead = 0

    override fun getRefreshKey(state: PagingState<LocalDate, CalendarDay>): LocalDate? =
        state.anchorPosition?.let { state.closestItemToPosition(it)?.date }

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, CalendarDay> {
        val start = params.key ?: LocalDate.now()
        val end = start.plusDays(windowDays.toLong())

        val response = when (val result = provider(start.minusDays(1), windowDays + 2)) {
            is SnacResult.Failure -> return LoadResult.Error(SnacException(result.error))
            is SnacResult.Success -> result.value
        }

        val days = response
            .asSequence()
            .mapNotNull { it.toCalendarItem() }
            .filter { it.date in start..<end }
            .groupBy { it.date }
            .map { (date, items) -> CalendarDay(date, items) }
            .sortedBy { it.date }
            .toList()

        if (params !is LoadParams.Prepend) {
            emptyWindowsAhead = if (days.isEmpty()) emptyWindowsAhead + 1 else 0
        }

        return LoadResult.Page(
            data = days,
            prevKey = start.minusDays(windowDays.toLong()),
            nextKey = if (emptyWindowsAhead >= maxEmptyWindowsAhead) null else end,
        )
    }
}
