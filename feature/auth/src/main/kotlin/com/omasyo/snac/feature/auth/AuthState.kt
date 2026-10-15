package com.omasyo.snac.feature.auth

sealed interface AuthState {
    data object Idle : AuthState

    data object Authorizing : AuthState

    data object Exchanging : AuthState

    data class Error(val message: String) : AuthState

    val isLoading get() = when(this) {
        Authorizing, Exchanging -> true
        else -> false
    }
}
