package com.omasyo.snac.core.network.search.models

import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = TrendingSearchSerializer::class)
sealed interface TrendingSearchResponse {
    val count: Int
}

@Serializable
data class TrendingSearchMovieResponse(
    @SerialName("count")
    override val count: Int,
    @SerialName("movie")
    val movie: MovieResponse,
) : TrendingSearchResponse

@Serializable
data class TrendingSearchShowResponse(
    @SerialName("count")
    override val count: Int,
    @SerialName("show")
    val show: ShowResponse,
) : TrendingSearchResponse

object TrendingSearchSerializer : JsonContentPolymorphicSerializer<TrendingSearchResponse>(
    TrendingSearchResponse::class
) {
    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> TrendingSearchMovieResponse.serializer()
        "show" in element.jsonObject -> TrendingSearchShowResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
