package com.omasyo.snac.core.data.calendar

import androidx.paging.PagingData
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.model.media.CalendarMediaType
import com.omasyo.snac.core.model.media.Genre
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarRepository {
    fun getAllMedia(
        startDate: LocalDate,
        type: CalendarMediaType,
        genre: Genre,
    ): Flow<PagingData<CalendarDay>>

    fun getHotReleases(startDate: LocalDate, genre: Genre): Flow<PagingData<CalendarDay>>
}
