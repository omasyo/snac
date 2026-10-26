package com.omasyo.snac.core.network.media.models


import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = AnticipatedMediaSerializer::class)
sealed interface AnticipatedMediaResponse {
    val listCount: Int
}

@Serializable
data class AnticipatedMovieResponse(
    @SerialName("list_count")
    override val listCount: Int,
    @SerialName("movie")
    val movie: MovieResponse,
) : AnticipatedMediaResponse

@Serializable
data class AnticipatedShowResponse(
    @SerialName("list_count")
    override val listCount: Int,
    @SerialName("show")
    val show: ShowResponse,
) : AnticipatedMediaResponse


object AnticipatedMediaSerializer : JsonContentPolymorphicSerializer<AnticipatedMediaResponse>(
    AnticipatedMediaResponse::class
) {

    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> AnticipatedMovieResponse.serializer()
        "show" in element.jsonObject -> AnticipatedShowResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
