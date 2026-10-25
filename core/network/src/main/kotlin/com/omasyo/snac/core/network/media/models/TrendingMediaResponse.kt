package com.omasyo.snac.core.network.media.models


import com.omasyo.snac.core.network.models.Movie
import com.omasyo.snac.core.network.models.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = MediaSerializer::class)
sealed interface TrendingItemResponse {
    val watchers: Int
}

@Serializable
data class MovieItemResponse(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("movie")
    val movie: Movie,
) : TrendingItemResponse

@Serializable
data class ShowItemResponse(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("show")
    val show: Show,
) : TrendingItemResponse


internal object MediaSerializer : JsonContentPolymorphicSerializer<TrendingItemResponse>(
    TrendingItemResponse::class
) {

    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> MovieItemResponse.serializer()
        "show" in element.jsonObject -> ShowItemResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
