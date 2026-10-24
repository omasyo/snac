package com.omasyo.snac.core.network.user

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.user.models.NetworkUser
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class UserNetworkSourceImpl(
    private val client: HttpClient
) : UserNetworkSource {
    override suspend fun getMe(): SnacResult<NetworkUser> = safeCall {
        client.get("users/me") {
            parameter("extended", "full")
        }
    }
}
