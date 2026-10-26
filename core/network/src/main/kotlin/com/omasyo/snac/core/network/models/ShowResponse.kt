package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("year")
    val year: Int,
    @SerialName("title")
    val title: String,
    @SerialName("aired_episodes")
    val airedEpisodes: Int
)