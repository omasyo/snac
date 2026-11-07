package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialIdsResponse(
    @SerialName("instagram")
    val instagram: String?,
)
