package com.omasyo.snac.core.model.media

import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

sealed interface PlaybackItem {
    val id: Long
    val media: Media
    val backdropUrl: String?
    val runtime: Duration?
    val progress: Float
    val pausedAt: LocalDateTime

    companion object {
        val PreviewSample: List<PlaybackItem> = List(10) {
            if (it % 2 == 0) {
                MoviePlayback(
                    id = it.toLong(),
                    media = Movie("movie-$it", "Frankenstein", 2025, null),
                    backdropUrl = null,
                    runtime = 149.minutes,
                    progress = 0.42f,
                    pausedAt = LocalDateTime.parse("2026-11-17T21:04:00"),
                )
            } else {
                EpisodePlayback(
                    id = it.toLong(),
                    media = Show("show-$it", "Dark", 2017, null),
                    episode = Episode(season = 1, number = 3, title = "Past and Present"),
                    backdropUrl = null,
                    runtime = 52.minutes,
                    progress = 0.8f,
                    pausedAt = LocalDateTime.parse("2026-11-18T22:30:00"),
                )
            }
        }
    }
}

data class MoviePlayback(
    override val id: Long,
    override val media: Movie,
    override val backdropUrl: String?,
    override val runtime: Duration?,
    override val progress: Float,
    override val pausedAt: LocalDateTime,
) : PlaybackItem

data class EpisodePlayback(
    override val id: Long,
    override val media: Show,
    val episode: Episode,
    override val backdropUrl: String?,
    override val runtime: Duration?,
    override val progress: Float,
    override val pausedAt: LocalDateTime,
) : PlaybackItem
