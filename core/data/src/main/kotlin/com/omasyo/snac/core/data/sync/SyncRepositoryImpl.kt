package com.omasyo.snac.core.data.sync

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toPlaybackItem
import com.omasyo.snac.core.data.mappers.toUpNextItem
import com.omasyo.snac.core.model.media.PlaybackItem
import com.omasyo.snac.core.model.media.UpNextItem
import com.omasyo.snac.core.network.sync.SyncNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class SyncRepositoryImpl(
    private val syncNetworkSource: SyncNetworkSource,
) : SyncRepository {
    override suspend fun getUpNext(): SnacResult<List<UpNextItem>> =
        syncNetworkSource.getUpNext(1, 10).map { items -> items.map { it.toUpNextItem() } }

    override suspend fun getPlayback(): SnacResult<List<PlaybackItem>> =
        syncNetworkSource.getPlayback(1, 10).map { items -> items.map { it.toPlaybackItem() } }
}
