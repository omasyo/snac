package com.omasyo.snac.core.network.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TokenStorage {
    val tokenFlow: Flow<Content?>

    suspend fun getToken(): Content?

    suspend fun saveToken(content: Content)

    suspend fun clearToken()


    @Serializable
    data class Content(
        @SerialName("access")
        val access: String,
        @SerialName("refresh")
        val refresh: String,
        @SerialName("expires_at")
        val expiresAt: Long,
    )
}
