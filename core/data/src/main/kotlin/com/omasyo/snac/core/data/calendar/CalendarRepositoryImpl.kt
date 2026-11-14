package com.omasyo.snac.core.data.calendar

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.data.paging.CalendarPagingSource
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.network.calendar.CalendarNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@ContributesBinding(AppScope::class)
@Inject
class CalendarRepositoryImpl(
    private val calendarNetworkSource: CalendarNetworkSource,
) : CalendarRepository {
    override fun getAllMedia(startDate: LocalDate): Flow<PagingData<CalendarDay>> =
        Pager(initialKey = startDate, config = pagingConfig) {
            CalendarPagingSource(
                windowDays = WINDOW_DAYS,
                provider = calendarNetworkSource::getAllMedia,
            )
        }.flow

    override fun getHotReleases(startDate: LocalDate): Flow<PagingData<CalendarDay>> =
        Pager(initialKey = startDate, config = pagingConfig) {
            CalendarPagingSource(
                windowDays = WINDOW_DAYS,
                provider = calendarNetworkSource::getHotReleases,
            )
        }.flow

    private companion object {
        const val WINDOW_DAYS = 7

        val pagingConfig = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
        )
    }
}
