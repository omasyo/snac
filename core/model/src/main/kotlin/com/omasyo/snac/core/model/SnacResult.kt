package com.omasyo.snac.core.model

sealed interface SnacResult<out T> {
    data class Success<T>(val value: T) : SnacResult<T>

    data class Failure(val error: Error) : SnacResult<Nothing>

    sealed interface Error {

        data object NoConnection : Error

        data object ServerUnavailable : Error

        data object NotFound : Error

        data class AccountLimitExceeded(val limit: String?) : Error

        data object AccountLocked : Error

        data class RateLimitExceeded(val retryAfterSeconds: Long?) : Error

        data object Unexpected : Error
    }
}

inline fun <T, R> SnacResult<T>.map(transform: (T) -> R): SnacResult<R> = when (this) {
    is SnacResult.Success -> SnacResult.Success(transform(value))
    is SnacResult.Failure -> this
}

inline fun <T> SnacResult<T>.onSuccess(action: (T) -> Unit): SnacResult<T> =
    also { if (it is SnacResult.Success) action(it.value) }

inline fun <T> SnacResult<T>.onFailure(action: (SnacResult.Error) -> Unit): SnacResult<T> =
    also { if (it is SnacResult.Failure) action(it.error) }
