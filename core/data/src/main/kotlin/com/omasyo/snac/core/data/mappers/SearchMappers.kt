package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.TrendingSearchItem
import com.omasyo.snac.core.network.search.models.TrendingSearchMovieResponse
import com.omasyo.snac.core.network.search.models.TrendingSearchResponse
import com.omasyo.snac.core.network.search.models.TrendingSearchShowResponse

fun TrendingSearchResponse.toTrendingSearchItem() = TrendingSearchItem(
    media = when (this) {
        is TrendingSearchMovieResponse -> movie.toMovie()
        is TrendingSearchShowResponse -> show.toShow()
    },
    count = count,
)
