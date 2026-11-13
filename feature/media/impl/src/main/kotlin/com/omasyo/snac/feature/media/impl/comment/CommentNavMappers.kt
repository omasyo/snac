package com.omasyo.snac.feature.media.impl.comment

import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.User
import com.omasyo.snac.feature.media.api.navigation.CommentThreadNavKey
import java.time.LocalDateTime

internal fun Comment.toThreadNavKey() = CommentThreadNavKey(
    id = id,
    comment = comment,
    isSpoiler = isSpoiler,
    isReview = isReview,
    repliesCount = repliesCount,
    likesCount = likesCount,
    rating = rating,
    userSlug = user.slug,
    userName = user.name,
    userImageUrl = user.imageUrl,
    createdAt = createdAt.toString(),
)

internal fun CommentThreadNavKey.toComment() = Comment(
    id = id,
    comment = comment,
    isSpoiler = isSpoiler,
    isReview = isReview,
    repliesCount = repliesCount,
    likesCount = likesCount,
    rating = rating,
    user = User(slug = userSlug, name = userName, imageUrl = userImageUrl),
    createdAt = LocalDateTime.parse(createdAt),
)
