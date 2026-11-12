package com.omasyo.snac.core.network.comment.models

import com.omasyo.snac.core.network.models.UserResponse
import com.omasyo.snac.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ReplyResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("comment")
    val comment: String,
    @SerialName("spoiler")
    val spoiler: Boolean,
    @SerialName("review")
    val review: Boolean,
    @SerialName("parent_id")
    val parentId: Long,
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
    val user: UserResponse,
)
