package com.omasyo.snac.core.data.auth

import android.net.Uri
import com.omasyo.snac.core.network.TraktConfig
import com.omasyo.snac.core.network.auth.AuthNetworkSource
import com.omasyo.snac.core.network.auth.models.toContent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
class AuthRepositoryImpl(
    private val tokenStorage: DataStoreTokenStorage,
    private val authNetworkSource: AuthNetworkSource,
) : AuthRepository {

    override val isSignedIn: Flow<Boolean> =
        tokenStorage.tokenFlow.map { it != null }.distinctUntilChanged()

    override suspend fun createAuthorizationUrl(): String {
        val pending = PendingAuthorization(
            state = generateState(),
            codeVerifier = generateCodeVerifier(),
        )
        tokenStorage.savePendingAuthorization(pending)

        return Uri.parse(TraktConfig.AuthorizeUrl).buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", TraktConfig.ClientId)
            .appendQueryParameter("redirect_uri", TraktConfig.RedirectUri)
            .appendQueryParameter("state", pending.state)
            .appendQueryParameter("code_challenge", generateCodeChallenge(pending.codeVerifier))
            .appendQueryParameter("code_challenge_method", "S256")
            .build()
            .toString()
    }

    override suspend fun completeAuthorization(redirectUri: String): Result<Unit> {
        val uri = Uri.parse(redirectUri)
        val pending = tokenStorage.getPendingAuthorization()
        if (pending == null || uri.getQueryParameter("state") != pending.state) {
            return Result.failure(IllegalStateException("State mismatch"))
        }

        tokenStorage.clearPendingAuthorization()

        uri.getQueryParameter("error")?.let { error ->
            return Result.failure(
                IllegalStateException(uri.getQueryParameter("error_description") ?: error)
            )
        }
        val code = uri.getQueryParameter("code")
            ?: return Result.failure(IllegalStateException("Missing code"))

        return authNetworkSource.exchangeCode(code, pending.codeVerifier).map {
            tokenStorage.saveToken(it.toContent())
            authNetworkSource.clearCachedTokens()
        }
    }

    override suspend fun signOut() {
        tokenStorage.getToken()?.let { token ->
            authNetworkSource.revoke(token.access)
        }
        tokenStorage.clearToken()
        authNetworkSource.clearCachedTokens()
    }
}
