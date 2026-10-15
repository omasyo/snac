package com.omasyo.snac.core.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.omasyo.snac.core.network.auth.TokenStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

/** State + PKCE verifier for an authorization that is in flight (survives process death). */
@Serializable
data class PendingAuthorization(
    val state: String,
    val codeVerifier: String,
)

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class DataStoreTokenStorage(
    context: Context,
) : TokenStorage {
    private val dataStore = context.authDataStore

    override val tokenFlow: Flow<TokenStorage.Content?> = dataStore.data.map { prefs ->
        prefs[TokenKey]?.let { Json.decodeFromString<TokenStorage.Content>(it) }
    }

    override suspend fun getToken(): TokenStorage.Content? = tokenFlow.first()

    override suspend fun saveToken(content: TokenStorage.Content) {
        dataStore.edit { it[TokenKey] = Json.encodeToString(content) }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(TokenKey) }
    }

    suspend fun getPendingAuthorization(): PendingAuthorization? =
        dataStore.data.first()[PendingKey]?.let { Json.decodeFromString(it) }

    suspend fun savePendingAuthorization(pending: PendingAuthorization) {
        dataStore.edit { it[PendingKey] = Json.encodeToString(pending) }
    }

    suspend fun clearPendingAuthorization() {
        dataStore.edit { it.remove(PendingKey) }
    }

    private companion object {
        val TokenKey = stringPreferencesKey("token")
        val PendingKey = stringPreferencesKey("pending_authorization")
    }
}
