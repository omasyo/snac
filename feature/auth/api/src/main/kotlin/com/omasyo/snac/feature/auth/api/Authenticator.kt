package com.omasyo.snac.feature.auth.api

import kotlinx.coroutines.flow.StateFlow

interface Authenticator {
    val state: StateFlow<AuthState>

    val isSignedIn: StateFlow<Boolean>

    fun signIn()

    fun signOut()
}
