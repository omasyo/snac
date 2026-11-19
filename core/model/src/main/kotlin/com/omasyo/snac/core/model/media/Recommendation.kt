package com.omasyo.snac.core.model.media

data class Recommendation(
    val media: Media,
    val genre: Genre?,
) {
    companion object {
        val MoviePreviewSample = List(10) {
            Recommendation(
                media = Movie("movie-$it", "Orbital Diner", 2025, null),
                genre = Genre("Comedy", "comedy"),
            )
        }
        val ShowPreviewSample = List(10) {
            Recommendation(
                media = Show("show-$it", "Saltwater Ledger", 2024, null),
                genre = Genre("Drama", "drama"),
            )
        }
    }
}
