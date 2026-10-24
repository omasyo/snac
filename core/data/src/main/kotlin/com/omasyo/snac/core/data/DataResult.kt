package com.omasyo.snac.core.data

import com.omasyo.snac.core.network.NetworkResult

sealed interface DataResult<out T> {
    data class Success<T>(val value: T) : DataResult<T>

    data class Failure(val error: DataError) : DataResult<Nothing>
}

sealed interface DataError {

    data object Connection : DataError

    data object ServerUnavailable : DataError

    data object NotFound : DataError

    data class AccountLimitExceeded(val limit: String?) : DataError

    data object LockedAccount : DataError

    data class RateLimitExceeded(val retryAfterSeconds: Long?) : DataError

    data object ErrorOnMyPart : DataError
}

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Success -> DataResult.Success(transform(value))
    is DataResult.Failure -> this
}

inline fun <T> DataResult<T>.onSuccess(action: (T) -> Unit): DataResult<T> =
    also { if (it is DataResult.Success) action(it.value) }

inline fun <T> DataResult<T>.onFailure(action: (DataError) -> Unit): DataResult<T> =
    also { if (it is DataResult.Failure) action(it.error) }

internal fun <T> NetworkResult<T>.toDataResult(): DataResult<T> = when (this) {
    is NetworkResult.Success -> DataResult.Success(value)
    is NetworkResult.Failure -> DataResult.Failure(error.toDataError())
}

private fun NetworkResult.Error.toDataError(): DataError = when (this) {
    NetworkResult.Error.Connection -> DataError.Connection
    NetworkResult.Error.ServerUnavailable -> DataError.ServerUnavailable
    NetworkResult.Error.NotFound -> DataError.NotFound
    is NetworkResult.Error.AccountLimitExceeded -> DataError.AccountLimitExceeded(limit)
    NetworkResult.Error.LockedAccount -> DataError.LockedAccount
    is NetworkResult.Error.RateLimitExceeded -> DataError.RateLimitExceeded(retryAfterSeconds)
    is NetworkResult.Error.ErrorOnMyPart -> DataError.ErrorOnMyPart
}
