package com.omasyo.snac.core.network.media.models


import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = TrendingMediaSerializer::class)
sealed interface TrendingMediaResponse {
    val watchers: Int
}

@Serializable
data class TrendingMovieResponse(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("movie")
    val movie: MovieResponse,
) : TrendingMediaResponse

@Serializable
data class TrendingShowResponse(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("show")
    val show: ShowResponse,
) : TrendingMediaResponse


 object TrendingMediaSerializer : JsonContentPolymorphicSerializer<TrendingMediaResponse>(
    TrendingMediaResponse::class
) {

    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> TrendingMovieResponse.serializer()
        "show" in element.jsonObject -> TrendingShowResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
