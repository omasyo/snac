package com.omasyo.snac.core.network.calendar

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ContributesBinding(AppScope::class)
@Inject
class CalendarNetworkSourceImpl(
    private val client: HttpClient
) : CalendarNetworkSource {
    override suspend fun getAllMedia(
        startDate: LocalDate,
        days: Int
    ): SnacResult<List<CalendarMediaResponse>> =
        safeCall {
            client.get("calendars/all/media/${startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}/$days") {
                parameter("group", "day")
                parameter("genres", "action")
            }
        }

    override suspend fun getHotReleases(
        startDate: LocalDate,
        days: Int
    ): SnacResult<List<CalendarMediaResponse>> =
        safeCall {
            client.get("calendars/releases/hot/${startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}/$days") {
                parameter("group", "day")
                parameter("genres", "action")
            }
        }
}
