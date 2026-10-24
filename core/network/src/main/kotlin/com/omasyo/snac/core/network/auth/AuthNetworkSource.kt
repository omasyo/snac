package com.omasyo.snac.core.network.auth

import com.omasyo.snac.core.network.NetworkResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.auth.models.TokenResponse

interface AuthNetworkSource : NetworkSource {
    suspend fun exchangeCode(code: String, codeVerifier: String): NetworkResult<TokenResponse>

    suspend fun revoke(accessToken: String): NetworkResult<Unit>

    fun clearCachedTokens()
}
