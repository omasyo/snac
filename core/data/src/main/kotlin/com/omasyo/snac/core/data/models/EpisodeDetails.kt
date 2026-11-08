package com.omasyo.snac.core.data.models

import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class EpisodeType {
    Standard,
    SeriesPremiere,
    SeasonPremiere,
    MidSeasonFinale,
    MidSeasonPremiere,
    SeasonFinale,
    SeriesFinale,
}

data class EpisodeDetails(
    val season: Int,
    val number: Int,
    val title: String?,
    val overview: String?,
    val screenshotUrl: String?,
    val rating: Double,
    val runtime: Duration?,
    val type: EpisodeType,
    val firstAired: LocalDate?,
    val commentCount: Int,
) {
    companion object {
        val PreviewSample = List(10) {
            EpisodeDetails(
                season = 1,
                number = it + 1,
                title = "Secrets",
                overview = "A boy goes missing in the woods outside town, and the search stirs up an older disappearance that nobody wants to talk about.",
                screenshotUrl = null,
                rating = 8.2,
                runtime = 52.minutes,
                type = if (it == 0) EpisodeType.SeriesPremiere else EpisodeType.Standard,
                firstAired = LocalDate.of(2017, 12, 1),
                commentCount = 42,
            )
        }
    }
}
