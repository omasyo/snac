package com.omasyo.snac.core.data.models

import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class Genre(val name: String)

enum class ReleaseStatus { Released } //TODO other release statuses

data class SocialIds(val instagram: String?)

// Checkout videos
// Comments extended or not makes no difference

data class MovieDetails(
    val slug: String,
    val title: String,
    val year: Int,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
    val genres: List<Genre>,
    val rating: Double,
    val status: ReleaseStatus,
    val runtime: Duration,
    val tagline: String?,
    val homepage: String?,
    val trailer: String?,
    val country: String,
    val language: String,
    val overview: String,
    val releaseDate: LocalDate,
    val socialIds: SocialIds,
    val certification: String,
    val commentCount: Int,
    val originalTitle: String,
) {
    companion object {
        val PreviewSample = MovieDetails(
            slug = "slug",
            title = "Frankenstein",
            year = 2025,
            posterUrl = null,
            secondaryPosterUrl = null,
            genres = listOf(Genre("Drama"), Genre("Horror")),
            rating = 7.5,
            status = ReleaseStatus.Released,
            runtime = 90.minutes,
            tagline = "Only monsters play God.",
            homepage = "www.website.com",
            trailer = "www.youtube.com",
            country = "us",
            language = "en",
            overview = "Dr. Victor Frankenstein, a brilliant but egotistical scientist, brings a creature to life in a monstrous experiment that ultimately leads to the undoing of both the creator and his tragic creation.",
            releaseDate = LocalDate.now(),
            socialIds = SocialIds(instagram = null),
            certification = "PG-13",
            commentCount = 123,
            originalTitle = "Frankenstein"
        )
    }
}

//data class Comment()
