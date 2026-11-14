package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.CalendarItem
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.EpisodeAiring
import com.omasyo.snac.core.model.media.MovieRelease
import com.omasyo.snac.core.network.calendar.models.CalendarEpisodeResponse
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import com.omasyo.snac.core.network.calendar.models.CalendarMovieResponse
import com.omasyo.snac.core.network.calendar.models.CalendarShowResponse
import java.time.ZoneId

fun CalendarMediaResponse.toCalendarItem(zone: ZoneId = ZoneId.systemDefault()): CalendarItem? =
    when (this) {
        is CalendarMovieResponse -> released?.let { MovieRelease(media = movie.toMovie(), date = it) }
        is CalendarShowResponse -> EpisodeAiring(
            media = show.toShow(),
            episode = episode.toEpisode(),
            episodes = episode.episodes.orEmpty().map { it.toEpisode() },
            firstAired = firstAired,
            date = firstAired.atZone(zone).toLocalDate(),
        )
    }

fun CalendarEpisodeResponse.toEpisode() = Episode(
    season = season,
    number = number,
    title = title,
)
