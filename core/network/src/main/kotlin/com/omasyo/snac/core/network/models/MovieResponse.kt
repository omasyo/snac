package com.omasyo.snac.core.network.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import com.omasyo.snac.core.network.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
data class MovieResponse(
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
    @SerialName("released")
    @Serializable(with = LocalDateSerializer::class)
    val released: LocalDate?,
    @SerialName("social_ids")
    val socialIds: SocialIdsResponse?,
    @SerialName("certification")
    val certification: String?,
    @SerialName("comment_count")
    val commentCount: Int,
    @SerialName("original_title")
    val originalTitle: String?,
    @SerialName("after_credits")
    val afterCredits: Boolean?,
    @SerialName("during_credits")
    val duringCredits: Boolean?,
    @SerialName("updated_at")
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
)
