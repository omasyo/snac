package com.omasyo.snac.core.network.movie.models


import com.omasyo.snac.core.network.models.MovieResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostFavoritedMovieResponse(
    @SerialName("user_count")
     val userCount: Int,
    @SerialName("movie")
    val movie: MovieResponse,
)