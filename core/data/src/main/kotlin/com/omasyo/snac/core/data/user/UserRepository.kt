package com.omasyo.snac.core.data.user

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.user.UserProfile

interface UserRepository {
    suspend fun getProfile(): SnacResult<UserProfile>
}
