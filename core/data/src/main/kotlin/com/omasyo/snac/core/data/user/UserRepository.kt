package com.omasyo.snac.core.data.user

import com.omasyo.snac.core.model.user.UserProfile
import com.omasyo.snac.core.network.user.UserNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
}

@ContributesBinding(AppScope::class)
@Inject
class UserRepositoryImpl(
    private val userNetworkSource: UserNetworkSource,
) : UserRepository {
    override suspend fun getProfile(): Result<UserProfile> =
        userNetworkSource.getMe().map {
            UserProfile(
                username = it.username,
                name = it.name,
                isVip = it.vip,
                avatarUrl = it.images?.avatar?.full,
            )
        }
}
