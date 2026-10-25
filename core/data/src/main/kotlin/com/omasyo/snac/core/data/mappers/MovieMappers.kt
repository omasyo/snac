package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.network.movie.models.BoxOfficeResponse
import com.omasyo.snac.core.network.models.Movie as NetworkMovie

fun NetworkMovie.toMovie() = Movie(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)

fun BoxOfficeResponse.toBoxOfficeItem() = BoxOfficeItem(
    media = movie.toMovie(),
    revenue = revenue,
)