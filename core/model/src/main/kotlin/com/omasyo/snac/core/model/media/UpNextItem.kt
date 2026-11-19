package com.omasyo.snac.core.model.media

data class UpNextItem(
    val show: Show,
    val nextEpisode: Episode,
    val backdropUrl: String?,
    val aired: Int,
    val completed: Int,
) {
    companion object {
        val PreviewSample = List(10) {
            UpNextItem(
                show = Show("show-$it", "Dark", 2017, null),
                nextEpisode = Episode(season = 2, number = 4, title = "The Travelers"),
                backdropUrl = null,
                aired = 26,
                completed = 11,
            )
        }
    }
}
