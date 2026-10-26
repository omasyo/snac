package com.omasyo.snac.core.network.movie.models


import com.omasyo.snac.core.network.models.MovieResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostMovieStatsResponse(
    @SerialName("watcher_count")
    val watcherCount: Int,
    @SerialName("play_count")
    val playCount: Int,
    @SerialName("collected_count")
    val collectedCount: Int,
    @SerialName("movie")
    val movie: MovieResponse,
)