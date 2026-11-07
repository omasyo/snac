package com.omasyo.snac.core.network.movie.models

import com.omasyo.snac.core.network.models.Ids
import com.omasyo.snac.core.network.models.ImagesResponse
import com.omasyo.snac.core.network.models.SocialIdsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("images")
    val images: ImagesResponse? = null,
    @SerialName("genres")
    val genres: List<String> = emptyList(),
    @SerialName("rating")
    val rating: Double = 0.0,
    @SerialName("status")
    val status: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("trailer")
    val trailer: String? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("released")
    val released: String? = null,
    @SerialName("social_ids")
    val socialIds: SocialIdsResponse? = null,
    @SerialName("certification")
    val certification: String? = null,
    @SerialName("comment_count")
    val commentCount: Int = 0,
    @SerialName("original_title")
    val originalTitle: String? = null,
)
