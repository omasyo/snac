package com.omasyo.snac.core.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isSignedIn: Flow<Boolean>

    suspend fun createAuthorizationUrl(): String

    suspend fun completeAuthorization(redirectUri: String): Result<Unit>

    suspend fun signOut()
}
