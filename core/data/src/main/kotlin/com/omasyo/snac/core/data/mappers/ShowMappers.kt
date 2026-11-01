package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.CollectedItem
import com.omasyo.snac.core.model.media.FavoritedItem
import com.omasyo.snac.core.model.media.PlayedItem
import com.omasyo.snac.core.model.media.PopularItem
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.WatchedItem
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse

fun ShowResponse.toShow() = Show(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)

fun ShowResponse.toPopularItem() = PopularItem(
    media = toShow()
)

fun AnticipatedShowResponse.toAnticipatedItem() = AnticipatedItem(
    media = show.toShow(),
    listCount = listCount,
)

fun MostShowStatsResponse.toCollectedItem() = CollectedItem(
    media = show.toShow(),
    collectedCount = collectedCount,
    collectorCount = collectorCount,
)

fun MostFavoritedShowResponse.toFavoritedItem() = FavoritedItem(
    media = show.toShow(),
    userCount = userCount,
)

fun MostShowStatsResponse.toPlayedItem() = PlayedItem(
    media = show.toShow(),
    playCount = playCount,
)

fun MostShowStatsResponse.toWatchedItem() = WatchedItem(
    media = show.toShow(),
    watcherCount = watcherCount,
)