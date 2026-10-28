package com.omasyo.snac.core.network.movie.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingResponse(
    @SerialName("trakt")
    val trakt: Trakt,
    @SerialName("tmdb")
    val tmdb: Tmdb,
    @SerialName("imdb")
    val imdb: Imdb,
    @SerialName("metascore")
    val metascore: Metascore,
    @SerialName("rotten_tomatoes")
    val rottenTomatoes: RottenTomatoes,
    @SerialName("mal")
    val mal: Mal,
    @SerialName("letterboxd")
    val letterboxd: Letterboxd
) {
    @Serializable
    data class Trakt(
        @SerialName("rating")
        val rating: Double,
        @SerialName("votes")
        val votes: Int,
        @SerialName("distribution")
        val distribution: Distribution
    ) {
        @Serializable
        data class Distribution(
            @SerialName("1")
            val x1: Int,
            @SerialName("2")
            val x2: Int,
            @SerialName("3")
            val x3: Int,
            @SerialName("4")
            val x4: Int,
            @SerialName("5")
            val x5: Int,
            @SerialName("6")
            val x6: Int,
            @SerialName("7")
            val x7: Int,
            @SerialName("8")
            val x8: Int,
            @SerialName("9")
            val x9: Int,
            @SerialName("10")
            val x10: Int
        )
    }

    @Serializable
    data class Tmdb(
        @SerialName("rating")
        val rating: Double,
        @SerialName("votes")
        val votes: Int,
        @SerialName("link")
        val link: String
    )

    @Serializable
    data class Imdb(
        @SerialName("rating")
        val rating: Double,
        @SerialName("votes")
        val votes: Int,
        @SerialName("link")
        val link: String
    )

    @Serializable
    data class Metascore(
        @SerialName("link")
        val link: String,
        @SerialName("rating")
        val rating: Int
    )

    @Serializable
    data class RottenTomatoes(
        @SerialName("link")
        val link: String,
        @SerialName("rating")
        val rating: Int,
        @SerialName("state")
        val state: String,
        @SerialName("user_rating")
        val userRating: Int,
        @SerialName("user_state")
        val userState: String
    )

    @Serializable
    data class Mal(
        @SerialName("rating")
        val rating: Double?,
        @SerialName("votes")
        val votes: Int,
        @SerialName("link")
        val link: String?
    )

    @Serializable
    data class Letterboxd(
        @SerialName("rating")
        val rating: Double,
        @SerialName("votes")
        val votes: Int,
        @SerialName("link")
        val link: String
    )
}