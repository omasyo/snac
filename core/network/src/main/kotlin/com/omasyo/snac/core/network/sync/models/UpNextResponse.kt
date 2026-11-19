package com.omasyo.snac.core.network.sync.models

import com.omasyo.snac.core.network.models.EpisodeResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class UpNextResponse(
    @SerialName("show")
    val show: ShowResponse,
    @SerialName("progress")
    val progress: Progress,
) {
    @Serializable
    data class Progress(
        @SerialName("aired")
        val aired: Int,
        @SerialName("completed")
        val completed: Int,
        @SerialName("hidden")
        val hidden: Int,
        @SerialName("stats")
        val stats: Stats,
        @SerialName("reset_at")
        @Serializable(with = InstantSerializer::class)
        val resetAt: Instant?,
        @SerialName("last_watched_at")
        @Serializable(with = InstantSerializer::class)
        val lastWatchedAt: Instant?,
        @SerialName("last_episode")
        val lastEpisode: EpisodeResponse?,
        @SerialName("next_episode")
        val nextEpisode: EpisodeResponse,
    )

    @Serializable
    data class Stats(
        @SerialName("play_count")
        val playCount: Int,
        @SerialName("minutes_left")
        val minutesLeft: Int,
        @SerialName("minutes_watched")
        val minutesWatched: Int,
    )
}
