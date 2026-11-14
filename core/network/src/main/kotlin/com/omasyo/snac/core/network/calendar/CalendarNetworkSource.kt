package com.omasyo.snac.core.network.calendar

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import java.time.LocalDate

interface CalendarNetworkSource : NetworkSource {
    suspend fun getAllMedia(startDate: LocalDate, days: Int): SnacResult<List<CalendarMediaResponse>>

    suspend fun getHotReleases(startDate: LocalDate, days: Int): SnacResult<List<CalendarMediaResponse>>
}
