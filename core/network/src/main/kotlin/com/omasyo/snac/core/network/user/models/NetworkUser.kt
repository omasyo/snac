package com.omasyo.snac.core.network.user.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("username")
    val username: String,
    @SerialName("name")
    val name: String?,
    @SerialName("vip")
    val vip: Boolean,
    @SerialName("images")
    val images: Images?,
) {
    @Serializable
    data class Images(
        @SerialName("avatar")
        val avatar: Avatar?,
    )

    @Serializable
    data class Avatar(
        @SerialName("full")
        val full: String?,
    )
}
