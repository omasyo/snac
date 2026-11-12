package com.omasyo.snac.core.data.models

import java.time.LocalDateTime

data class User( // TODO might be it's own model
    val slug: String,
    val name: String,
    val imageUrl: String?,
)

data class Comment(
    val id: Long,
    val comment: String,
    val isSpoiler: Boolean,
    val isReview: Boolean,
    val repliesCount: Int,
    val likesCount: Int,
    val rating: Int?,
    val user: User,
    val createdAt: LocalDateTime,
) {
    companion object {
        val PreviewSample = List(20) {
            Comment(
                id = it.toLong(),
                comment = "Nice music but a weird and trashy movie. In theory, it is a horror movie, but come on, it is far from that. Don’t watch it!",
                isSpoiler = it % 3 == 0,
                isReview = it % 2 == 0,
                repliesCount = 20,
                likesCount = 458,
                rating = 7,
                user = User(
                    slug = "user",
                    name = "omasyo",
                    imageUrl = null
                ),
                createdAt = LocalDateTime.now().minusDays(6),
            )
        }
    }
}
