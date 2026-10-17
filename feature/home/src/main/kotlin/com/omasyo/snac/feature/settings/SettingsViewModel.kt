package com.omasyo.snac.feature.settings

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProfileState {
    data object Idle : ProfileState
    data object Loading : ProfileState
    data class Loaded(val profile: UserProfile) : ProfileState
    data class Failed(val message: String) : ProfileState
}

@ContributesIntoMap(AppScope::class, binding<ViewModel>())
@ViewModelKey(SettingsViewModel::class)
@Inject
class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val profileState: StateFlow<ProfileState> field = MutableStateFlow<ProfileState>(ProfileState.Idle)


    fun fetchProfile() {
        profileState.value = ProfileState.Loading
        viewModelScope.launch {
            profileState.value = userRepository.getProfile().fold(
                onSuccess = { ProfileState.Loaded(it) },
                onFailure = { ProfileState.Failed(it.message ?: "Unknown error") },
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            profileState.value = ProfileState.Idle
        }
    }

    init {
        fetchProfile()
    }
}
