package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.EpisodeType
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.data.models.ShowEnding
import com.omasyo.snac.core.data.models.ShowStatus
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.CollectedItem
import com.omasyo.snac.core.model.media.FavoritedItem
import com.omasyo.snac.core.model.media.PlayedItem
import com.omasyo.snac.core.model.media.PopularItem
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.WatchedItem
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse
import com.omasyo.snac.core.network.show.models.EpisodeDetailsResponse
import com.omasyo.snac.core.network.show.models.SeasonResponse
import com.omasyo.snac.core.network.show.models.ShowDetailsResponse
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.Duration.Companion.minutes

fun ShowResponse.toShow() = Show(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)

fun ShowResponse.toPopularItem() = PopularItem(
    media = toShow()
)

fun AnticipatedShowResponse.toAnticipatedItem() = AnticipatedItem(
    media = show.toShow(),
    listCount = listCount,
)

fun MostShowStatsResponse.toCollectedItem() = CollectedItem(
    media = show.toShow(),
    collectedCount = collectedCount,
    collectorCount = collectorCount,
)

fun MostFavoritedShowResponse.toFavoritedItem() = FavoritedItem(
    media = show.toShow(),
    userCount = userCount,
)

fun MostShowStatsResponse.toPlayedItem() = PlayedItem(
    media = show.toShow(),
    playCount = playCount,
)

fun MostShowStatsResponse.toWatchedItem() = WatchedItem(
    media = show.toShow(),
    watcherCount = watcherCount,
)

fun ShowDetailsResponse.toShowDetails() = ShowDetails(
    slug = ids.slug,
    title = title,
    year = year,
    posterUrl = images?.poster?.firstOrNull()?.toImageUrl(),
    secondaryPosterUrl = images?.fanart?.firstOrNull()?.toImageUrl()
        ?: images?.poster?.getOrNull(1)?.toImageUrl(),
    genres = genres.map { it.toGenre() },
    rating = rating,
    status = status?.toShowStatus(),
    runtime = runtime?.takeIf { it > 0 }?.minutes,
    tagline = tagline?.takeIf { it.isNotBlank() },
    homepage = homepage?.takeIf { it.isNotBlank() },
    trailer = trailer?.takeIf { it.isNotBlank() },
    country = country?.takeIf { it.isNotBlank() },
    language = language?.takeIf { it.isNotBlank() },
    overview = overview?.takeIf { it.isNotBlank() },
    firstAired = firstAired?.let { LocalDate.ofInstant(it, ZoneId.systemDefault()) },
    lastAired = lastAired?.let { LocalDate.ofInstant(it, ZoneId.systemDefault()) },
    ending = ending?.toShowEnding(),
    network = network?.takeIf { it.isNotBlank() },
    airedEpisodes = airedEpisodes,
    socialIds = socialIds.toSocialIds(),
    certification = certification?.takeIf { it.isNotBlank() },
    commentCount = commentCount,
    originalTitle = originalTitle ?: title,
)

private fun String.toShowStatus(): ShowStatus? = when (this) {
    "returning series" -> ShowStatus.ReturningSeries
    "continuing" -> ShowStatus.Continuing
    "in production" -> ShowStatus.InProduction
    "planned" -> ShowStatus.Planned
    "upcoming" -> ShowStatus.Upcoming
    "pilot" -> ShowStatus.Pilot
    "canceled" -> ShowStatus.Canceled
    "ended" -> ShowStatus.Ended
    else -> null
}

private fun String.toShowEnding(): ShowEnding? = when (this) {
    "concluded" -> ShowEnding.Concluded
    "open" -> ShowEnding.Open
    else -> null
}

fun SeasonResponse.toSeason(): Season {
    val title = title?.takeIf { it.isNotBlank() } ?: "Season $number"
    return Season(
        number = number,
        title = title,
        overview = overview?.takeIf { it.isNotBlank() },
        posterUrl = images?.poster?.firstOrNull()?.toImageUrl(),
        rating = rating,
        episodeCount = episodeCount,
        airedEpisodes = airedEpisodes,
        totalRuntime = totalRuntime?.takeIf { it > 0 }?.minutes,
        firstAired = firstAired?.let { LocalDate.ofInstant(it, ZoneId.systemDefault()) },
        network = network?.takeIf { it.isNotBlank() },
        originalTitle = originalTitle?.takeIf { it.isNotBlank() },
    )
}

fun EpisodeDetailsResponse.toEpisodeDetails() = EpisodeDetails(
    season = season,
    number = number,
    title = title?.takeIf { it.isNotBlank() },
    overview = overview?.takeIf { it.isNotBlank() },
    screenshotUrl = images?.screenshot?.firstOrNull()?.toImageUrl(),
    rating = rating,
    runtime = runtime?.takeIf { it > 0 }?.minutes,
    type = episodeType.toEpisodeType(),
    firstAired = firstAired?.let { LocalDate.ofInstant(it, ZoneId.systemDefault()) },
    commentCount = commentCount,
)

private fun String?.toEpisodeType(): EpisodeType = when (this) {
    "series_premiere" -> EpisodeType.SeriesPremiere
    "season_premiere" -> EpisodeType.SeasonPremiere
    "mid_season_finale" -> EpisodeType.MidSeasonFinale
    "mid_season_premiere" -> EpisodeType.MidSeasonPremiere
    "season_finale" -> EpisodeType.SeasonFinale
    "series_finale" -> EpisodeType.SeriesFinale
    else -> EpisodeType.Standard
}
