package com.omasyo.snac.core.model.media

import java.time.Instant
import java.time.LocalDate

sealed interface CalendarItem {
    val media: Media
    val date: LocalDate
}

data class MovieRelease(
    override val media: Movie,
    override val date: LocalDate,
) : CalendarItem

data class EpisodeAiring(
    override val media: Show,
    val episode: Episode,
    val episodes: List<Episode>,
    val firstAired: Instant,
    override val date: LocalDate,
) : CalendarItem

data class CalendarDay(
    val date: LocalDate,
    val items: List<CalendarItem>,
)
