package com.omasyo.snac.core.data.auth

import com.omasyo.snac.core.data.DataResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isSignedIn: Flow<Boolean>

    val sessionExpired: Flow<Unit>

    suspend fun createAuthorizationUrl(): String

    suspend fun completeAuthorization(redirectUri: String): DataResult<AuthorizationOutcome>

    suspend fun signOut()
}

enum class AuthorizationOutcome {
    SignedIn,

    Cancelled,
}
