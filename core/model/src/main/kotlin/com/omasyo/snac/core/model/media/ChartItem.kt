package com.omasyo.snac.core.model.media

sealed interface ChartItem {
    val media: Media
}

data class TrendingItem(
    override val media: Media,
    val watchers: Int,
) : ChartItem {
    companion object {
        val MoviePreviewSample = List(10) {
            TrendingItem(Movie("movie-$it", "Frankenstein", 2025, null), watchers = 1204)
        }
        val ShowPreviewSample = List(10) {
            TrendingItem(Show("show-$it", "Dark", 2017, null), watchers = 300)
        }
    }
}

data class PopularItem(
    override val media: Media,
) : ChartItem

data class AnticipatedItem(
    override val media: Media,
    val listCount: Int,
) : ChartItem {
    companion object {
        val PreviewSample = List(10) {
            AnticipatedItem(Movie("movie-$it", "Frankenstein", 2025, null), listCount = 1204)
        }
    }
}

data class MediaStatsItem(
    override val media: Media,
    val watcherCount: Int,
    val playCount: Int,
    val collectedCount: Int,
    val collectorCount: Int?,
) : ChartItem

data class FavoritedItem(
    override val media: Media,
    val userCount: Int,
) : ChartItem

data class BoxOfficeItem(
    override val media: Movie,
    val revenue: Long,
) : ChartItem {
    companion object {
        val PreviewSample = listOf(
            BoxOfficeItem(Movie("1", "Harmattan", 2025, null), 21_700_000),
            BoxOfficeItem(Movie("2", "Orbital Diner", 2025, null), 12_900_000),
            BoxOfficeItem(Movie("3", "Ghost Frequency", 2025, null), 8_400_000),
            BoxOfficeItem(Movie("4", "Velvet Hour", 2025, null), 5_100_000),
            BoxOfficeItem(Movie("5", "Saltline", 2025, null), 3_300_000),
        )
    }
}
