package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.network.models.Movie as NetworkMovie

fun NetworkMovie.toMovie() = Movie(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)