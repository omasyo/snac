package com.omasyo.snac.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.auth.AuthRepository
import com.omasyo.snac.core.data.user.UserRepository
import com.omasyo.snac.core.model.user.UserProfile
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class, binding<ViewModel>())
@ViewModelKey(AuthViewModel::class)
@Inject
class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val authState: StateFlow<AuthState> field  = MutableStateFlow<AuthState>(AuthState.Idle)


    private val _authorizeUrls = Channel<String>(Channel.BUFFERED)
    val authorizeUrls: Flow<String> = _authorizeUrls.receiveAsFlow()

    fun signIn() {
        authState.value = AuthState.Authorizing
        viewModelScope.launch {
            _authorizeUrls.send(authRepository.createAuthorizationUrl())
        }
    }

    fun onRedirect(uri: String) {
        authState.value = AuthState.Exchanging
        viewModelScope.launch {
            authRepository.completeAuthorization(uri)
                .onSuccess { authState.value = AuthState.Idle }
                .onFailure { authState.value = AuthState.Error(it.message ?: "Unknown error") }
        }
    }
}
