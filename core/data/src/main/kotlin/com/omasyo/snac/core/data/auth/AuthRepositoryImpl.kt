package com.omasyo.snac.core.data.auth

import android.net.Uri
import android.util.Log
import com.omasyo.snac.core.data.DataError
import com.omasyo.snac.core.data.DataResult
import com.omasyo.snac.core.data.map
import com.omasyo.snac.core.data.toDataResult
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

    override val sessionExpired: Flow<Unit> = tokenStorage.sessionExpired

    override suspend fun createAuthorizationUrl(): String {
        val pending = PendingAuthorization(
            state = generateState(),
            codeVerifier = generateCodeVerifier(),
        )
        tokenStorage.savePendingAuthorization(pending)

        return Uri.parse(TraktConfig.AUTHORIZE_URL).buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", TraktConfig.CLIENT_ID)
            .appendQueryParameter("redirect_uri", TraktConfig.REDIRECT_URI)
            .appendQueryParameter("state", pending.state)
            .appendQueryParameter("code_challenge", generateCodeChallenge(pending.codeVerifier))
            .appendQueryParameter("code_challenge_method", "S256")
            .build()
            .toString()
    }

    override suspend fun completeAuthorization(redirectUri: String): DataResult<AuthorizationOutcome> {
        val uri = Uri.parse(redirectUri)
        val pending = tokenStorage.getPendingAuthorization()
        if (pending == null || uri.getQueryParameter("state") != pending.state) {
            return authorizationFailed("State mismatch")
        }

        tokenStorage.clearPendingAuthorization()

        uri.getQueryParameter("error")?.let { error ->
            if (error == "access_denied") return DataResult.Success(AuthorizationOutcome.Cancelled)
            return authorizationFailed(uri.getQueryParameter("error_description") ?: error)
        }
        val code = uri.getQueryParameter("code")
            ?: return authorizationFailed("Missing code")

        return authNetworkSource.exchangeCode(code, pending.codeVerifier).toDataResult().map {
            tokenStorage.saveToken(it.toContent())
            authNetworkSource.clearCachedTokens()
            AuthorizationOutcome.SignedIn
        }
    }

    private fun authorizationFailed(detail: String): DataResult<Nothing> {
        Log.e("AuthRepository", "completeAuthorization: $detail")
        return DataResult.Failure(DataError.ErrorOnMyPart)
    }

    override suspend fun signOut() {
        tokenStorage.getToken()?.let { token ->
            authNetworkSource.revoke(token.access)
        }
        tokenStorage.clearToken()
        authNetworkSource.clearCachedTokens()
    }
}
