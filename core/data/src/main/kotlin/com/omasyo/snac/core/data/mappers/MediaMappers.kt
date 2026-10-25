package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.media.models.AnticipatedMediaResponse
import com.omasyo.snac.core.network.media.models.AnticipatedMovieResponse
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.media.models.TrendingMediaResponse
import com.omasyo.snac.core.network.media.models.TrendingMovieResponse
import com.omasyo.snac.core.network.media.models.TrendingShowResponse

fun AnticipatedMediaResponse.toAnticipatedItem() = AnticipatedItem(
    media = when (this) {
        is AnticipatedMovieResponse -> movie.toMovie()
        is AnticipatedShowResponse -> show.toShow()
    },
    listCount = listCount,
)

fun TrendingMediaResponse.toTrendingItem() = TrendingItem(
    media = when (this) {
        is TrendingMovieResponse -> movie.toMovie()
        is TrendingShowResponse -> show.toShow()
    },
    watchers = watchers,
)