package com.omasyo.snac.core.network.auth.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeTokenRequest(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("redirect_uri")
    val redirectUri: String,
    @SerialName("code")
    val code: String,
    @SerialName("grant_type")
    val grantType: String,
    @SerialName("code_verifier")
    val codeVerifier: String
)