package com.omasyo.snac.core.model

sealed interface SnacError {

    data object Connection : SnacError

    data object ServerUnavailable : SnacError

    data object NotFound : SnacError

    data class AccountLimitExceeded(val limit: String?) : SnacError

    data object LockedAccount : SnacError

    data class RateLimitExceeded(val retryAfterSeconds: Long?) : SnacError

    data object ErrorOnMyPart : SnacError
}
