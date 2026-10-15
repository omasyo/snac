package com.omasyo.snac.core.network

import android.util.Log
import com.omasyo.snac.core.network.auth.TokenStorage
import com.omasyo.snac.core.network.auth.models.RefreshTokenRequest
import com.omasyo.snac.core.network.auth.models.TokenResponse
import com.omasyo.snac.core.network.auth.models.toContent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createClient(tokenStorage: TokenStorage) = HttpClient(OkHttp) {
    expectSuccess = false
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("HttpClient", message)
            }
        }
        level = LogLevel.HEADERS
    }
    install(Auth) {
        bearer {
            loadTokens {
                tokenStorage.getToken()?.let { BearerTokens(it.access, it.refresh) }
            }
            refreshTokens {
                val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                val response = runCatching {
                    client.post(TraktConfig.TokenUrl) {
                        markAsRefreshTokenRequest()
                        setBody(
                            RefreshTokenRequest(
                                clientId = TraktConfig.ClientId,
                                redirectUri = TraktConfig.RedirectUri,
                                refreshToken = refreshToken,
                            )
                        )
                    }
                }.getOrNull()

                if (response?.status?.isSuccess() == true) {
                    val content = response.body<TokenResponse>().toContent()
                    tokenStorage.saveToken(content)
                    BearerTokens(content.access, content.refresh)
                } else {
                    if (response != null) tokenStorage.clearToken()
                    null
                }
            }
            sendWithoutRequest { request -> request.url.host == TraktConfig.ApiHost }
        }
    }
    defaultRequest {
        url(TraktConfig.ApiBaseUrl)
        contentType(ContentType.Application.Json)
        userAgent("Snac/0.1") // TODO Hardcoded version
        header("trakt-api-version", "2")
        header("trakt-api-key", TraktConfig.ClientId)
    }
}