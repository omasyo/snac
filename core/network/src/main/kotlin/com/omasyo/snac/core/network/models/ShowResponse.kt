package com.omasyo.snac.core.network.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ShowResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: Int?,
    @SerialName("images")
    val images: ImagesResponse?,
    @SerialName("genres")
    val genres: List<String>,
    @SerialName("rating")
    val rating: Double,
    @SerialName("votes")
    val votes: Int?,
    @SerialName("status")
    val status: String?,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("total_runtime")
    val totalRuntime: Int?,
    @SerialName("tagline")
    val tagline: String?,
    @SerialName("trailer")
    val trailer: String?,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("language")
    val language: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("first_aired")
    @Serializable(with = InstantSerializer::class)
    val firstAired: Instant?,
    @SerialName("last_aired")
    @Serializable(with = InstantSerializer::class)
    val lastAired: Instant?,
    @SerialName("airs")
    val airs: Airs?,
    @SerialName("ending")
    val ending: String?, //TODO check what this entails
    @SerialName("network")
    val network: String?,
    @SerialName("aired_episodes")
    val airedEpisodes: Int,
    @SerialName("social_ids")
    val socialIds: SocialIdsResponse?,
    @SerialName("certification")
    val certification: String?,
    @SerialName("comment_count")
    val commentCount: Int,
    @SerialName("original_title")
    val originalTitle: String?,
    @SerialName("updated_at")
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
) {
    @Serializable
    data class Airs(
        @SerialName("day")
        val day: String?,
        @SerialName("time")
        val time: String?,
        @SerialName("timezone")
        val timezone: String?,
    )
}
