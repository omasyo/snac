package com.omasyo.snac.core.network.di

import android.util.Log
import com.omasyo.snac.core.network.TraktConfig
import com.omasyo.snac.core.network.auth.TokenStorage
import com.omasyo.snac.core.network.auth.models.RefreshTokenRequest
import com.omasyo.snac.core.network.auth.models.TokenResponse
import com.omasyo.snac.core.network.auth.models.toContent
import com.omasyo.snac.core.network.createClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@ContributesTo(AppScope::class)
interface NetworkProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(tokenStorage: TokenStorage): HttpClient = createClient(tokenStorage)
}
