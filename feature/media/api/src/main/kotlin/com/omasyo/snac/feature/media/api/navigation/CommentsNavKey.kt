package com.omasyo.snac.feature.media.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface CommentsNavKey : NavKey

@Serializable
data class MovieCommentsNavKey(val slug: String) : CommentsNavKey

@Serializable
data class ShowCommentsNavKey(val slug: String) : CommentsNavKey

@Serializable
data class CommentThreadNavKey(val commentId: Long) : NavKey
