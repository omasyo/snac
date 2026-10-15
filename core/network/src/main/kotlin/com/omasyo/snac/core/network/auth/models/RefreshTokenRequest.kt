package com.omasyo.snac.core.network.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("redirect_uri")
    val redirectUri: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("grant_type")
    val grantType: String = "refresh_token",
)
