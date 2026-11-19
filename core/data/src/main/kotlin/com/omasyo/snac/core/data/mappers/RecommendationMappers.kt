package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.genre.MovieGenres
import com.omasyo.snac.core.data.genre.ShowGenres
import com.omasyo.snac.core.model.media.Recommendation
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse

fun MovieResponse.toRecommendation() = Recommendation(
    media = toMovie(),
    genre = genres.firstNotNullOfOrNull { slug -> MovieGenres.find { it.slug == slug } },
)

fun ShowResponse.toRecommendation() = Recommendation(
    media = toShow(),
    genre = genres.firstNotNullOfOrNull { slug -> ShowGenres.find { it.slug == slug } },
)
