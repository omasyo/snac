package com.omasyo.snac.core.network.movie.models


import com.omasyo.snac.core.network.models.MovieResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostMovieStatsResponse(
    @SerialName("watcher_count")
    val watcherCount: Long,
    @SerialName("play_count")
    val playCount: Long,
    @SerialName("collected_count")
    val collectedCount: Long,
    @SerialName("movie")
    val movie: MovieResponse,
)