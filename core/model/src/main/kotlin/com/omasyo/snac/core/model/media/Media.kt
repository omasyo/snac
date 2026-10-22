package com.omasyo.snac.core.model.media

sealed interface Media {
    val slug: String
    val title: String
    val year: Int
    val imageUrl: String?
}

data class Movie(
    override val slug: String,
    override val title: String,
    override val year: Int,
    override val imageUrl: String?,
) : Media

data class Show(
    override val slug: String,
    override val title: String,
    override val year: Int,
    override val imageUrl: String?,
) : Media

data class Episode(
    val season: Int,
    val number: Int,
    val title: String?,
)
