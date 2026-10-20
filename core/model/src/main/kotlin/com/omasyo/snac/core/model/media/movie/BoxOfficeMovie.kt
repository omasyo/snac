package com.omasyo.snac.core.model.media.movie

data class BoxOfficeMovie(
    override val slug: String,
    override val title: String,
    val revenue: String,
) : Movie {
    companion object {
        val PreviewSample = listOf(
            BoxOfficeMovie("1", "Harmattan", "$21.7M"),
            BoxOfficeMovie("2", "Orbital Diner", "$12.9M"),
            BoxOfficeMovie("3", "Ghost Frequency", "$8.4"),
            BoxOfficeMovie("4", "Velvet Hour", "$5.1M"),
            BoxOfficeMovie("5", "Saltline", "$3.3M"),
        )
    }
}