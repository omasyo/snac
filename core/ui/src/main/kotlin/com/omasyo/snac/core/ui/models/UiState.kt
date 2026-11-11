package com.omasyo.snac.core.ui.models

import com.omasyo.snac.core.common.SnacResult

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Error(val error: SnacResult.Error, val refresh: () -> Unit) : UiState<Nothing>

    data class Success<T>(val content: T, val isRefreshing: Boolean = false) : UiState<T>
}

inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Error -> UiState.Error(error, refresh)
    UiState.Loading -> UiState.Loading
    is UiState.Success -> UiState.Success(transform(content), isRefreshing)
}

inline fun <T, R> UiState<T>.mapOrNull(transform: (T) -> R): R? = when (this) {
    is UiState.Success -> transform(content)
    else -> null
}

fun <T> UiState<T?>.transpose(): UiState<T>? = when (this) {
    is UiState.Error -> UiState.Error(error, refresh)
    UiState.Loading -> UiState.Loading
    is UiState.Success -> content?.let { UiState.Success(it, isRefreshing) }
}

//fun <T> SnacResult<T>.toUiState() : UiState<T>  = when (this) {
//    is SnacResult.Error -> UiState.Error(this) {}
//    is SnacResult.Success -> UiState.Success(data)
//}
