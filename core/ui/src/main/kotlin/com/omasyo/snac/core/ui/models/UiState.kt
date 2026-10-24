package com.omasyo.snac.core.ui.models

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Error(val error: SnacError, val refresh: () -> Unit) :
        UiState<Nothing> //TODO error type

    data class Success<T>(val content: T, val isRefreshing: Boolean = false) : UiState<T>
}

inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Error -> UiState.Error(error, refresh)
    UiState.Loading -> UiState.Loading
    is UiState.Success -> UiState.Success(transform(content), isRefreshing)
}

fun <T> UiState<T?>.transpose(): UiState<T>? = when (this) {
    is UiState.Error -> UiState.Error(error, refresh)
    UiState.Loading -> UiState.Loading
    is UiState.Success -> content?.let { UiState.Success(it, isRefreshing) }
}

