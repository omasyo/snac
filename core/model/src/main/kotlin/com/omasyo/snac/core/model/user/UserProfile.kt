package com.omasyo.snac.core.model.user

data class UserProfile(
    val username: String,
    val name: String?,
    val isVip: Boolean,
    val avatarUrl: String?,
)
