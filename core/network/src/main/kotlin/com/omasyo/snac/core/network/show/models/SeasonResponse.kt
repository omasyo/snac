package com.omasyo.snac.core.network.show.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SeasonResponse(
    @SerialName("number")
    val number: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("original_title")
    val originalTitle: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("images")
    val images: Images?,
    @SerialName("rating")
    val rating: Double,
    @SerialName("episode_count")
    val episodeCount: Int,
    @SerialName("aired_episodes")
    val airedEpisodes: Int,
    @SerialName("total_runtime")
    val totalRuntime: Int?,
    @SerialName("first_aired")
    @Serializable(with = InstantSerializer::class)
    val firstAired: Instant?,
    @SerialName("network")
    val network: String?,
) {
    @Serializable
    data class Images(
        @SerialName("poster")
        val poster: List<String>,
    )
}
