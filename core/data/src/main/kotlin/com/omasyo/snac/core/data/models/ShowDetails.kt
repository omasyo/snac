package com.omasyo.snac.core.data.models

import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class ShowStatus {
    ReturningSeries,
    Continuing,
    InProduction,
    Planned,
    Upcoming,
    Pilot,
    Canceled,
    Ended,
}

enum class ShowEnding { //TODO check what this entails
    Concluded,

    Open,
}

data class ShowDetails(
    val slug: String,
    val title: String,
    val year: Int?,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
    val genres: List<Genre>,
    val rating: Double,
    val status: ShowStatus?,
    val runtime: Duration?,
    val tagline: String?,
    val homepage: String?,
    val trailer: String?,
    val country: String?,
    val language: String?,
    val overview: String?,
    val firstAired: LocalDate?,
    val lastAired: LocalDate?,
    val ending: ShowEnding?, //TODO check what this entails
    val network: String?,
    val airedEpisodes: Int,
    val socialIds: SocialIds,
    val certification: String?,
    val commentCount: Int,
    val originalTitle: String,
) {
    companion object {
        val PreviewSample = ShowDetails(
            slug = "slug",
            title = "Dark",
            year = 2017,
            posterUrl = null,
            secondaryPosterUrl = null,
            genres = listOf(Genre("Drama"), Genre("Mystery"), Genre("Science Fiction")),
            rating = 8.6,
            status = ShowStatus.Ended,
            runtime = 60.minutes,
            tagline = "Everything is connected.",
            homepage = "www.website.com",
            trailer = "www.youtube.com",
            country = "de",
            language = "de",
            overview = "A missing child sets four families on a search for answers, uncovering a mystery that spans several generations of a small German town.",
            firstAired = LocalDate.of(2017, 12, 1),
            lastAired = LocalDate.of(2020, 6, 27),
            ending = ShowEnding.Concluded,
            network = "Netflix",
            airedEpisodes = 26,
            socialIds = SocialIds.Empty,
            certification = "TV-MA",
            commentCount = 123,
            originalTitle = "Dark",
        )
    }
}

data class Season(
    val number: Int,
    val title: String,
    val overview: String?,
    val posterUrl: String?,
    val rating: Double,
    val episodeCount: Int,
    val airedEpisodes: Int,
    val totalRuntime: Duration,
    val firstAired: LocalDate?,
    val network: String?,
) {
    companion object {
        val PreviewSample = List(3) {
            Season(
                number = it + 1,
                title = "Season ${it + 1}",
                overview = null,
                posterUrl = null,
                rating = 8.5,
                episodeCount = 10,
                airedEpisodes = if (it == 2) 4 else 10,
                totalRuntime = 520.minutes,
                firstAired = LocalDate.of(2017 + it, 12, 1),
                network = "Netflix",
            )
        }
    }
}
