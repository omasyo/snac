package com.omasyo.snac.core.network.movie.models


import com.omasyo.snac.core.network.models.MovieResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoxOfficeResponse(
    @SerialName("revenue")
     val revenue: Long,
    @SerialName("movie")
    val movie: MovieResponse,
)