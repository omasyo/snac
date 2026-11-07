package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Review
import com.omasyo.snac.core.data.models.User
import com.omasyo.snac.core.model.extra.CommentSort
import com.omasyo.snac.core.network.models.CommentResponse
import java.time.LocalDateTime
import java.time.ZoneId
import com.omasyo.snac.core.network.models.CommentSort as NetworkCommentSort

fun CommentSort.toNetworkCommentSort(): NetworkCommentSort = when (this) {
    CommentSort.Newest -> NetworkCommentSort.Newest
    CommentSort.Oldest -> NetworkCommentSort.Oldest
    CommentSort.Likes -> NetworkCommentSort.Likes
    CommentSort.Replies -> NetworkCommentSort.Replies
    CommentSort.Highest -> NetworkCommentSort.Highest
    CommentSort.Lowest -> NetworkCommentSort.Lowest
    CommentSort.Plays -> NetworkCommentSort.Plays
}

fun CommentResponse.toReview() = Review(
    id = id,
    comment = comment,
    isSpoiler = spoiler,
    isReview = review,
    repliesCount = replies,
    likesCount = likes,
    rating = userRating,
    user = user.toUser(),
    createdAt = LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()),
)

private fun CommentResponse.User.toUser() = User(
    slug = ids.slug,
    name = name?.takeIf { it.isNotBlank() } ?: username,
    imageUrl = images?.avatar?.full,
)
