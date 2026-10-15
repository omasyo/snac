package com.omasyo.snac.feature.auth

sealed interface AuthState {
    data object Idle : AuthState

    data object Loading : AuthState

     data object Success : AuthState

    data class Error(val message: String) : AuthState
}