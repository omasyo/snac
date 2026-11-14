package com.omasyo.snac.core.data.calendar

import androidx.paging.PagingData
import com.omasyo.snac.core.model.media.CalendarDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarRepository {
    fun getAllMedia(startDate: LocalDate): Flow<PagingData<CalendarDay>>

    fun getHotReleases(startDate: LocalDate): Flow<PagingData<CalendarDay>>
}
