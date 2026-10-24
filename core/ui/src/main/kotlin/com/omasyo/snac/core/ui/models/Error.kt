package com.omasyo.snac.core.ui.models

import com.omasyo.snac.core.model.SnacResult

val SnacResult.Error.message: String
    get() = when (this) {
        SnacResult.Error.NoConnection -> "Check your internet connection and try again"
        SnacResult.Error.ServerUnavailable -> "Trakt is having trouble right now. Try again shortly"
        SnacResult.Error.NotFound -> "We couldn't find that"
        is SnacResult.Error.AccountLimitExceeded -> "You've reached your Trakt account limit"
        SnacResult.Error.AccountLocked -> "Your Trakt account is locked. Contact Trakt support"
        is SnacResult.Error.RateLimitExceeded -> "Too many requests. Try again shortly"
        SnacResult.Error.Unexpected -> "An error occurred"
    }
