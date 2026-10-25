package com.omasyo.snac.core.data.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toAnticipatedItem
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.media.MediaNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject


@ContributesBinding(AppScope::class)
@Inject
class MediaRepositoryImpl(
    private val mediaNetworkSource: MediaNetworkSource,
) : MediaRepository {
    override suspend fun getTopTrending(): SnacResult<TrendingItem?> =
        mediaNetworkSource.getTopTrending().map { it.firstOrNull()?.toTrendingItem() }

    override suspend fun getAnticipated(): SnacResult<List<AnticipatedItem>> =
        mediaNetworkSource.getAnticipated(1, 10).map { item -> item.map { it.toAnticipatedItem() } }
}