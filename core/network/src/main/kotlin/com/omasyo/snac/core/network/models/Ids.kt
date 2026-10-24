package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ids(
    @SerialName("slug")
    val slug: String,
    @SerialName("trakt")
    val trakt: Int
)