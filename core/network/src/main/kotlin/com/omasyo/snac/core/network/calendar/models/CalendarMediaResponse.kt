package com.omasyo.snac.core.network.calendar.models

import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.serializers.InstantSerializer
import com.omasyo.snac.core.network.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import java.time.Instant
import java.time.LocalDate

@Serializable(with = CalendarMediaSerializer::class)
sealed interface CalendarMediaResponse {
    val released: LocalDate?
}

@Serializable
data class CalendarMovieResponse(
    @SerialName("movie")
    val movie: MovieResponse,
    @SerialName("released")
    @Serializable(with = LocalDateSerializer::class)
    override val released: LocalDate?,
) : CalendarMediaResponse

@Serializable
data class CalendarShowResponse(
    @SerialName("show")
    val show: ShowResponse,
    @SerialName("episode")
    val episode: CalendarEpisodeResponse,
    @SerialName("first_aired")
    @Serializable(with = InstantSerializer::class)
    val firstAired: Instant,
    @SerialName("released")
    @Serializable(with = LocalDateSerializer::class)
    override val released: LocalDate?,
) : CalendarMediaResponse

@Serializable
data class CalendarEpisodeResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("title")
    val title: String?,
    @SerialName("number")
    val number: Int,
    @SerialName("season")
    val season: Int,
    @SerialName("episode_type")
    val episodeType: String? = null,
    @SerialName("episodes")
    val episodes: List<CalendarEpisodeResponse>? = null,
) {
    @Serializable
    data class Ids(
        @SerialName("trakt")
        val trakt: Int,
    )
}

object CalendarMediaSerializer : JsonContentPolymorphicSerializer<CalendarMediaResponse>(
    CalendarMediaResponse::class
) {
    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> CalendarMovieResponse.serializer()
        "show" in element.jsonObject -> CalendarShowResponse.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
