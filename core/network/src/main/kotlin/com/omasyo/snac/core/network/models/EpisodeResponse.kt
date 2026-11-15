package com.omasyo.snac.core.network.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class EpisodeResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("season")
    val season: Int,
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
    @SerialName("votes")
    val votes: Int?,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("episode_type")
    val episodeType: String?,
    @SerialName("first_aired")
    @Serializable(with = InstantSerializer::class)
    val firstAired: Instant?,
    @SerialName("comment_count")
    val commentCount: Int,
    @SerialName("after_credits")
    val afterCredits: Boolean?,
    @SerialName("during_credits")
    val duringCredits: Boolean?,
    @SerialName("updated_at")
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
) {
    @Serializable
    data class Ids(
        @SerialName("trakt")
        val trakt: Int,
    )

    @Serializable
    data class Images(
        @SerialName("screenshot")
        val screenshot: List<String>,
    )
}
