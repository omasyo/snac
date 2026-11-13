package com.omasyo.snac.feature.media.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface CommentsNavKey : NavKey

@Serializable
data class MovieCommentsNavKey(val slug: String) : CommentsNavKey

@Serializable
data class ShowCommentsNavKey(val slug: String) : CommentsNavKey

@Serializable
data class CommentThreadNavKey(
    val id: Long,
    val comment: String,
    val isSpoiler: Boolean,
    val isReview: Boolean,
    val repliesCount: Int,
    val likesCount: Int,
    val rating: Int?,
    val userSlug: String,
    val userName: String,
    val userImageUrl: String?,
    val createdAt: String,
) : NavKey
