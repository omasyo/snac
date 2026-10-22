package com.omasyo.snac.core.model.media

import com.omasyo.snac.core.model.media.movie.AnticipatedMovie

interface AnticipatedMedia : Media {
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