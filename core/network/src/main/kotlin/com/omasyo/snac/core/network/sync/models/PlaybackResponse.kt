package com.omasyo.snac.core.network.sync.models

import com.omasyo.snac.core.network.models.EpisodeResponse
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import java.time.Instant

@Serializable(with = PlaybackSerializer::class)
sealed interface PlaybackResponse {
    val id: Long
    val progress: Double
    val pausedAt: Instant
}

@Serializable
data class PlaybackMovieResponse(
    @SerialName("id")
    override val id: Long,
    @SerialName("progress")
    override val progress: Double,
    @SerialName("paused_at")
    @Serializable(with = InstantSerializer::class)
    override val pausedAt: Instant,
    @SerialName("movie")
    val movie: MovieResponse,
) : PlaybackResponse

@Serializable
data class PlaybackEpisodeResponse(
    @SerialName("id")
    override val id: Long,
    @SerialName("progress")
    override val progress: Double,
    @SerialName("paused_at")
    @Serializable(with = InstantSerializer::class)
    override val pausedAt: Instant,
    @SerialName("episode")
    val episode: EpisodeResponse,
    @SerialName("show")
    val show: ShowResponse,
) : PlaybackResponse

object PlaybackSerializer : JsonContentPolymorphicSerializer<PlaybackResponse>(
    PlaybackResponse::class
) {
    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> PlaybackMovieResponse.serializer()
        "episode" in element.jsonObject -> PlaybackEpisodeResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
