package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("year")
    val year: Int,
    @SerialName("title")
    val title: String
)