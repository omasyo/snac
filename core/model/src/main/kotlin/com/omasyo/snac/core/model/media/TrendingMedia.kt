package com.omasyo.snac.core.model.media

import com.omasyo.snac.core.model.media.movie.TrendingMovie

interface TrendingMedia : Media {
    val slug: String
    val title: String
    val watchingCount: Int
    val imageUrl: String?

    companion object {
        val PreviewSample = List(10) {
            TrendingMovie(
                slug = "movie-$it",
                title = "Frankenstein",
                watchingCount = 1204,
                year = 2025,
                imageUrl = null,
            )
        }
    }
}