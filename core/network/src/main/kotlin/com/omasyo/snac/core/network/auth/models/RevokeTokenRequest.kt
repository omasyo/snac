package com.omasyo.snac.core.network.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevokeTokenRequest(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("token")
    val token: String,
)
