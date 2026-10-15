package com.omasyo.snac.core.network.auth.models


import com.omasyo.snac.core.network.auth.TokenStorage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("scope")
    val scope: String,
    @SerialName("created_at")
    val createdAt: Long
)

fun TokenResponse.toContent() = TokenStorage.Content(
    access = accessToken,
    refresh = refreshToken,
    expiresAt = createdAt + expiresIn,
)
