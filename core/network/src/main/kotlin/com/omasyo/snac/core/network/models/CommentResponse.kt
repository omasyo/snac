package com.omasyo.snac.core.network.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CommentResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("comment")
    val comment: String,
    @SerialName("spoiler")
    val spoiler: Boolean,
    @SerialName("review")
    val review: Boolean,
    @SerialName("created_at")
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @SerialName("replies")
    val replies: Int,
    @SerialName("likes")
    val likes: Int,
    @SerialName("user_rating")
    val userRating: Int?,
    @SerialName("user")
    val user: User,
) {
    @Serializable
    data class User( // TODO user might be it's own model
        @SerialName("ids")
        val ids: Ids,
        @SerialName("username")
        val username: String,
        @SerialName("name")
        val name: String?,
        @SerialName("images")
        val images: Images?,
    )

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
