package com.omasyo.snac.core.model.media

sealed interface Media {
    val slug: String
    val title: String
    val year: Int?
    val imageUrl: String?
}

data class Movie(
    override val slug: String,
    override val title: String,
    override val year: Int?,
    override val imageUrl: String?,
) : Media {
    companion object {
        val PreviewSample = List(10) { Movie("movie-$it", "Frankenstein", 2025, null) }
    }
}

data class Show(
    override val slug: String,
    override val title: String,
    override val year: Int?,
    override val imageUrl: String?,
) : Media {
    companion object {
        val PreviewSample = List(10) { Show("show-$it", "Dark", 2017, null) }
    }
}

data class Episode(
    val season: Int,
    val number: Int,
    val title: String?,
)
