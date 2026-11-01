package com.omasyo.snac.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.auth.AuthRepository
import com.omasyo.snac.core.data.settings.SettingsRepository
import com.omasyo.snac.core.data.user.UserRepository
import com.omasyo.snac.core.model.settings.AppTheme
import com.omasyo.snac.core.model.settings.ThemeMode
import com.omasyo.snac.core.model.settings.ThemeSettings
import com.omasyo.snac.core.model.user.UserProfile
import com.omasyo.snac.core.ui.models.message
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProfileState {
    data object SignedOut : ProfileState
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
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val profileState: StateFlow<ProfileState> field = MutableStateFlow<ProfileState>(ProfileState.SignedOut)

    val themeSettings: StateFlow<ThemeSettings> = settingsRepository.themeSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ThemeSettings(),
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { settingsRepository.setTheme(theme) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }

    private suspend fun fetchProfile() {
        profileState.value = ProfileState.Loading
        profileState.value = when (val result = userRepository.getProfile()) {
            is SnacResult.Success -> ProfileState.Loaded(result.value)
            is SnacResult.Failure -> ProfileState.Failed(result.error.message)
        }
    }

    init {
        viewModelScope.launch {
            authRepository.isSignedIn.collectLatest { signedIn ->
                if (signedIn) fetchProfile() else profileState.value = ProfileState.SignedOut
            }
        }
    }
}
