package com.omasyo.snac.core.network.show.models


import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostShowStatsResponse(
    @SerialName("watcher_count")
    val watcherCount: Long,
    @SerialName("play_count")
    val playCount: Long,
    @SerialName("collected_count")
    val collectedCount: Long,
    @SerialName("show")
    val show: ShowResponse,
)