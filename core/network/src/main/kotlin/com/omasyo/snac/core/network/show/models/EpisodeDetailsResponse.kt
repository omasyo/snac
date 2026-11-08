package com.omasyo.snac.core.network.show.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class EpisodeDetailsResponse(
    @SerialName("season")
    val season: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("images")
    val images: Images?,
    @SerialName("rating")
    val rating: Double,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("episode_type")
    val episodeType: String?,
    @SerialName("first_aired")
    @Serializable(with = InstantSerializer::class)
    val firstAired: Instant?,
    @SerialName("comment_count")
    val commentCount: Int,
) {
    @Serializable
    data class Images(
        @SerialName("screenshot")
        val screenshot: List<String>,
    )
}
