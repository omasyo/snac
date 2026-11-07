package com.omasyo.snac.core.network.models

enum class CommentSort(val slug: String) {
    Newest("newest"),
    Oldest("oldest"),
    Likes("likes"),
    Replies("replies"),
    Highest("highest"),
    Lowest("lowest"),
    Plays("plays"),
}
