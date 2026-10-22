package com.omasyo.snac.core.model.media

import java.time.Instant
import java.time.LocalDate

sealed interface CalendarItem

data class MovieRelease(
    val movie: Movie,
    val released: LocalDate,
) : CalendarItem

data class EpisodeAiring(
    val episode: Episode,
    val show: Show,
    val firstAired: Instant,
) : CalendarItem
