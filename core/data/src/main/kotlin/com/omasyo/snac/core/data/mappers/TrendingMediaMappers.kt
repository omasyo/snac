package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.media.models.MovieItemResponse
import com.omasyo.snac.core.network.media.models.ShowItemResponse
import com.omasyo.snac.core.network.media.models.TrendingItemResponse

fun TrendingItemResponse.toTrendingItem() = TrendingItem(
    media = when (this) {
        is MovieItemResponse -> movie.toMovie()
        is ShowItemResponse -> show.toShow()
    },
    watchers = watchers,
)