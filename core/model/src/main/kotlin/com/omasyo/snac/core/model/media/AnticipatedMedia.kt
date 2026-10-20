package com.omasyo.snac.core.model.media

import com.omasyo.snac.core.model.media.movie.AnticipatedMovie
import com.omasyo.snac.core.model.media.movie.TrendingMovie

interface AnticipatedMedia {
    val slug: String
    val title: String
    val listCount: Int
    val imageUrl: String?

    companion object {
        val PreviewSample = List(10) {
            AnticipatedMovie(
                slug = "movie-$it",
                title = "Frankenstein",
                listCount = 1204,
                imageUrl = null,
            )
        }
    }
}