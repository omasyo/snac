package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialIdsResponse(
    @SerialName("twitter")
    val twitter: String?,
    @SerialName("facebook")
    val facebook: String?,
    @SerialName("instagram")
    val instagram: String?,
    @SerialName("wikipedia")
    val wikipedia: String?,
)
