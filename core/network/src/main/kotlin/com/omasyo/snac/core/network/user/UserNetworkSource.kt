package com.omasyo.snac.core.network.user

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.user.models.NetworkUser

interface UserNetworkSource : NetworkSource {
    suspend fun getMe(): SnacResult<NetworkUser>
}
