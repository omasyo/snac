package com.omasyo.snac.feature.calendar.impl

import androidx.paging.PagingData
import androidx.paging.flatMap
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.model.media.CalendarItem
import com.omasyo.snac.core.model.media.EpisodeAiring
import com.omasyo.snac.core.model.media.MovieRelease
import java.time.LocalDate

sealed interface CalendarUiItem {
    val date: LocalDate

    data class Header(override val date: LocalDate) : CalendarUiItem

    data class Entry(val item: CalendarItem) : CalendarUiItem {
        override val date: LocalDate get() = item.date
    }
}

internal fun PagingData<CalendarDay>.toUiItems(): PagingData<CalendarUiItem> =
    flatMap { day -> day.toUiItems() }

private fun CalendarDay.toUiItems(): List<CalendarUiItem> =
    listOf(CalendarUiItem.Header(date)) +
            items.map { CalendarUiItem.Entry(it) }

fun CalendarUiItem.key(): String = when(this) {
    is CalendarUiItem.Entry -> item.key()
    is CalendarUiItem.Header -> date.toString()
}

 fun CalendarItem.key(): String = when (this) {
    is MovieRelease -> "${media.slug}-$date"
    is EpisodeAiring -> "${media.slug}-$date-${episode.season}x${episode.number}"
}

internal fun List<CalendarUiItem>.indexOfDate(date: LocalDate): Int? {
    if (isEmpty() || date < first().date || date > last().date) return null
    return indexOfFirst { it.date >= date }
}
