package com.omasyo.snac.core.ui.models

import com.omasyo.snac.core.model.SnacError

val SnacError.message: String
    get() = when (this) {
        SnacError.Connection -> "Check your internet connection and try again"
        SnacError.ServerUnavailable -> "Trakt is having trouble right now. Try again shortly"
        SnacError.NotFound -> "We couldn't find that"
        is SnacError.AccountLimitExceeded -> "You've reached your Trakt account limit"
        SnacError.LockedAccount -> "Your Trakt account is locked. Contact Trakt support"
        is SnacError.RateLimitExceeded -> "Too many requests. Try again shortly"
        SnacError.ErrorOnMyPart -> "An error occurred"
    }
