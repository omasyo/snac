package com.omasyo.snac.core.network.auth

import com.omasyo.snac.core.network.auth.models.TokenResponse

interface AuthNetworkSource {
    suspend fun exchangeCode(code: String, codeVerifier: String): Result<TokenResponse>

    suspend fun revoke(accessToken: String): Result<Unit>

    fun clearCachedTokens()
}
