package com.omasyo.snac.core.data.user

import com.omasyo.snac.core.data.DataResult
import com.omasyo.snac.core.data.map
import com.omasyo.snac.core.data.toDataResult
import com.omasyo.snac.core.model.user.UserProfile
import com.omasyo.snac.core.network.user.UserNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface UserRepository {
    suspend fun getProfile(): DataResult<UserProfile>
}

@ContributesBinding(AppScope::class)
@Inject
class UserRepositoryImpl(
    private val userNetworkSource: UserNetworkSource,
) : UserRepository {
    override suspend fun getProfile(): DataResult<UserProfile> =
        userNetworkSource.getMe().toDataResult().map {
            UserProfile(
                username = it.username,
                name = it.name,
                isVip = it.vip,
                avatarUrl = it.images?.avatar?.full,
            )
        }
}
