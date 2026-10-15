package com.omasyo.snac.core.network.user

import com.omasyo.snac.core.network.user.models.NetworkUser

interface UserNetworkSource {
    suspend fun getMe(): Result<NetworkUser>
}
