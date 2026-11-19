package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.EpisodePlayback
import com.omasyo.snac.core.model.media.MoviePlayback
import com.omasyo.snac.core.model.media.PlaybackItem
import com.omasyo.snac.core.model.media.UpNextItem
import com.omasyo.snac.core.network.sync.models.PlaybackEpisodeResponse
import com.omasyo.snac.core.network.sync.models.PlaybackMovieResponse
import com.omasyo.snac.core.network.sync.models.PlaybackResponse
import com.omasyo.snac.core.network.sync.models.UpNextResponse
import java.time.ZoneId
import kotlin.time.Duration.Companion.minutes

fun UpNextResponse.toUpNextItem() = UpNextItem(
    show = show.toShow(),
    nextEpisode = progress.nextEpisode.toEpisode(),
    backdropUrl = (progress.nextEpisode.images?.screenshot?.firstOrNull()
        ?: show.images?.fanart?.firstOrNull())?.toImageUrl(),
    aired = progress.aired,
    completed = progress.completed,
)

fun PlaybackResponse.toPlaybackItem(zone: ZoneId = ZoneId.systemDefault()): PlaybackItem =
    when (this) {
        is PlaybackMovieResponse -> MoviePlayback(
            id = id,
            media = movie.toMovie(),
            backdropUrl = movie.images?.fanart?.firstOrNull()?.toImageUrl(),
            runtime = movie.runtime?.takeIf { it > 0 }?.minutes,
            progress = (progress / 100).toFloat(),
            pausedAt = pausedAt.atZone(zone).toLocalDateTime(),
        )

        is PlaybackEpisodeResponse -> EpisodePlayback(
            id = id,
            media = show.toShow(),
            episode = episode.toEpisode(),
            backdropUrl = (episode.images?.screenshot?.firstOrNull()
                ?: show.images?.fanart?.firstOrNull())?.toImageUrl(),
            runtime = episode.runtime?.takeIf { it > 0 }?.minutes,
            progress = (progress / 100).toFloat(),
            pausedAt = pausedAt.atZone(zone).toLocalDateTime(),
        )
    }
