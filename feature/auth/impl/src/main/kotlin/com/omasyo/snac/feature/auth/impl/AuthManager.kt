package com.omasyo.snac.feature.auth.impl

import android.net.Uri
import androidx.core.net.toUri
import com.omasyo.snac.core.data.auth.AuthRepository
import com.omasyo.snac.core.data.onFailure
import com.omasyo.snac.core.data.onSuccess
import com.omasyo.snac.core.ui.models.GenericError
import com.omasyo.snac.core.network.TraktConfig
import com.omasyo.snac.feature.auth.api.AuthState
import com.omasyo.snac.feature.auth.api.Authenticator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class AuthManager(
    private val authRepository: AuthRepository,
) : Authenticator {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    override val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _authorizationUrls = Channel<String>(Channel.BUFFERED)

    internal val authorizationUrls: Flow<String> = _authorizationUrls.receiveAsFlow()

    private val redirectUri = TraktConfig.REDIRECT_URI.toUri()

    override fun signIn() {
        if (_state.value.isLoading) return

        _state.value = AuthState.Authorizing
        scope.launch {
            runCatching { authRepository.createAuthorizationUrl() }
                .onSuccess { _authorizationUrls.send(it) }
                .onFailure { _state.value = AuthState.Error(it.message ?: "Unknown error") }
        }
    }

    override fun signOut() {
        scope.launch {
            authRepository.signOut()
        }
    }

    internal fun handleRedirect(uri: Uri): Boolean {
        if (uri.scheme != redirectUri.scheme || uri.host != redirectUri.host) return false

        _state.value = AuthState.Exchanging
        scope.launch {
            authRepository.completeAuthorization(uri.toString())
                .onSuccess { _state.value = AuthState.Idle }
                .onFailure { _state.value = AuthState.Error(GenericError.message) } //TODO message per DataError
        }
        return true
    }

    internal fun onResumed() {
        if (_state.value == AuthState.Authorizing) _state.value = AuthState.Idle
    }
}
