package com.omasyo.snac.core.data.user

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.model.user.UserProfile
import com.omasyo.snac.core.network.user.UserNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class UserRepositoryImpl(
    private val userNetworkSource: UserNetworkSource,
) : UserRepository {
    override suspend fun getProfile(): SnacResult<UserProfile> =
        userNetworkSource.getMe().map {
            UserProfile(
                username = it.username,
                name = it.name,
                isVip = it.vip,
                avatarUrl = it.images?.avatar?.full,
            )
        }
}