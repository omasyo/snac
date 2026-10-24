package com.omasyo.snac.core.network

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders

sealed interface NetworkResult<out T> {
    data class Success<T>(val value: T) : NetworkResult<T>

    data class Failure(val error: Error) : NetworkResult<Nothing>

    sealed interface Error {

        data object Connection : Error

        data object ServerUnavailable : Error

        data object NotFound : Error

        data class AccountLimitExceeded(val limit: String?) : Error

        data object LockedAccount : Error

        data class RateLimitExceeded(val retryAfterSeconds: Long?) : Error

        data class ErrorOnMyPart(val detail: String) : Error
    }
}

internal suspend fun HttpResponse.toNetworkError(): NetworkResult.Error = when (status.value) {
    404 -> NetworkResult.Error.NotFound
    420 -> NetworkResult.Error.AccountLimitExceeded(headers["X-Account-Limit"])
    423 -> NetworkResult.Error.LockedAccount
    429 -> NetworkResult.Error.RateLimitExceeded(headers[HttpHeaders.RetryAfter]?.toLongOrNull())
    in 500..599 -> NetworkResult.Error.ServerUnavailable
    else -> NetworkResult.Error.ErrorOnMyPart("HTTP ${status.value}: ${bodyAsText()}")
}
