package com.omasyo.snac.core.model.media

import com.omasyo.snac.core.model.media.movie.Movie
import com.omasyo.snac.core.model.media.show.Show

interface ChartMedia {
    val title: String
    val year: Int
    val imageUrl: String?
}

interface ChartShow : ChartMedia, Show

interface ChartMovie : ChartMedia, Movie

data class TrendingMovie(
    override val slug: String,
    override val title: String,
    override val year: Int,
    override val imageUrl: String?,
) : ChartMovie