package com.omasyo.snac.core.data.models

data class Rating(
    val title: String,
    val rating: String,
    val extra: String,
    val outOf: String? = null,
)

data class Ratings(
    val traktRating: Double,
    val voteCount: Int,
    val distribution: Map<String, Int>,
    val externalRatings: List<Rating>,
) {
    companion object {
        val PreviewSample = Ratings(
            traktRating = 8.56438,
            voteCount = 40067,
            distribution = mapOf(
                "1" to 89,
                "2" to 83,
                "3" to 59,
                "4" to 262,
                "5" to 570,
                "6" to 2444,
                "7" to 4187,
                "8" to 10738,
                "9" to 6179,
                "10" to 14600,
            ),
            externalRatings = listOf(
                Rating(
                    title = "TMDB",
                    rating = "8.5",
                    extra = "6,209 votes",
                    outOf = "10",
                ),
                Rating(
                    title = "IMDb",
                    rating = "7.9",
                    extra = "366,130 votes",
                    outOf = "10",
                ),
                Rating(
                    title = "Tomatometer",
                    rating = "90%",
                    extra = "Fresh",
                ),
                Rating(
                    title = "Popcornometer",
                    rating = "97%",
                    extra = "Upright",
                ),
                Rating(
                    title = "Metascore",
                    rating = "66",
                    extra = "Critic reviews",
                    outOf = "100",
                ),
                Rating(
                    title = "Letterboxd",
                    rating = "4.03",
                    extra = "4,621,098 votes",
                    outOf = "5",
                ),
            ),
        )
    }
}
