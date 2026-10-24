package com.omasyo.snac.core.network.auth

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.network.TraktConfig
import com.omasyo.snac.core.network.auth.models.ExchangeTokenRequest
import com.omasyo.snac.core.network.auth.models.RevokeTokenRequest
import com.omasyo.snac.core.network.auth.models.TokenResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody

@ContributesBinding(AppScope::class)
@Inject
class AuthNetworkSourceImpl(
    private val client: HttpClient
) : AuthNetworkSource {
    override suspend fun exchangeCode(code: String, codeVerifier: String): SnacResult<TokenResponse> =
        safeCall {
            client.post(TraktConfig.TOKEN_URL) {
                setBody(
                    ExchangeTokenRequest(
                        clientId = TraktConfig.CLIENT_ID,
                        redirectUri = TraktConfig.REDIRECT_URI,
                        code = code,
                        grantType = "authorization_code",
                        codeVerifier = codeVerifier,
                    )
                )
            }
        }

    override suspend fun revoke(accessToken: String): SnacResult<Unit> =
        safeCall {
            client.post(TraktConfig.REVOKE_URL) {
                setBody(RevokeTokenRequest(clientId = TraktConfig.CLIENT_ID, token = accessToken))
            }
        }

    override fun clearCachedTokens() {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
