package com.omasyo.snac.core.network.show.models


import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostShowStatsResponse(
    @SerialName("watcher_count")
    val watcherCount: Int,
    @SerialName("play_count")
    val playCount: Int,
    @SerialName("collected_count")
    val collectedCount: Int,
    @SerialName("collector_count")
    val collectorCount: Int?,
    @SerialName("show")
    val show: ShowResponse,
)