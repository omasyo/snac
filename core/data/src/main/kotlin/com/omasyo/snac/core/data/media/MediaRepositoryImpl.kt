package com.omasyo.snac.core.data.media

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toTrendingItem
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
    override suspend fun getTopTrendingMedia(): SnacResult<TrendingItem?> =
        mediaNetworkSource.getTopTrendingMedia().map { it.firstOrNull()?.toTrendingItem() }
}