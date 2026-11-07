package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    @SerialName("poster")
    val poster: List<String>,
    @SerialName("fanart")
    val fanart: List<String>,
)
