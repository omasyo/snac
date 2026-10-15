package com.omasyo.snac.core.network.user.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("username")
    val username: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vip")
    val vip: Boolean = false,
    @SerialName("images")
    val images: Images? = null,
) {
    @Serializable
    data class Images(
        @SerialName("avatar")
        val avatar: Avatar? = null,
    )

    @Serializable
    data class Avatar(
        @SerialName("full")
        val full: String? = null,
    )
}
