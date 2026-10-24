package com.omasyo.snac.core.network.auth

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.auth.models.TokenResponse

interface AuthNetworkSource : NetworkSource {
    suspend fun exchangeCode(code: String, codeVerifier: String): SnacResult<TokenResponse>

    suspend fun revoke(accessToken: String): SnacResult<Unit>

    fun clearCachedTokens()
}
