package com.omasyo.snac.core.network.search.models

import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.person.models.PersonDetailsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchResponse(
    @SerialName("score")
    val score: Double,
    @SerialName("movie")
    val movie: MovieResponse,
)

@Serializable
data class ShowSearchResponse(
    @SerialName("score")
    val score: Double,
    @SerialName("show")
    val show: ShowResponse,
)

@Serializable
data class PersonSearchResponse(
    @SerialName("score")
    val score: Double,
    @SerialName("person")
    val person: PersonDetailsResponse,
)
