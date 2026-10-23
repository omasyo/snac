package com.omasyo.snac.core.ui.models

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Error(val error: SnacError, val refresh: () -> Unit) :
        UiState<Nothing> //TODO error type

    data class Success<T>(val content: T, val isRefreshing: Boolean = false) : UiState<T>
}