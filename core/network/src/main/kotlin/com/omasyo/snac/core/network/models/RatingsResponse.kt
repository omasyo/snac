package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingsResponse(
    @SerialName("trakt")
    val trakt: Trakt,
    @SerialName("tmdb")
    val tmdb: Scored?,
    @SerialName("imdb")
    val imdb: Scored?,
    @SerialName("metascore")
    val metascore: Metascore?,
    @SerialName("rotten_tomatoes")
    val rottenTomatoes: RottenTomatoes?,
    @SerialName("mal")
    val mal: Scored?,
    @SerialName("letterboxd")
    val letterboxd: Scored?,
) {
    @Serializable
    data class Trakt(
        @SerialName("rating")
        val rating: Double,
        @SerialName("votes")
        val votes: Int,
        @SerialName("distribution")
        val distribution: Map<String, Int>,
    )

    @Serializable
    data class Scored(
        @SerialName("rating")
        val rating: Double?,
        @SerialName("votes")
        val votes: Int?,
    )

    @Serializable
    data class Metascore(
        @SerialName("rating")
        val rating: Int?,
    )

    @Serializable
    data class RottenTomatoes(
        @SerialName("rating")
        val rating: Int?,
        @SerialName("state")
        val state: String?,
        @SerialName("user_rating")
        val userRating: Int?,
        @SerialName("user_state")
        val userState: String?,
    )
}
