package com.omasyo.snac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.auth.AuthRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking


@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class MainViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val initialAuthenticatedState = runBlocking {
        authRepository.isSignedIn.first()
    }

    val isAuthenticated: StateFlow<Boolean> = authRepository.isSignedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialAuthenticatedState
    )

}