package com.omasyo.snac.core.data.sync

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.PlaybackItem
import com.omasyo.snac.core.model.media.UpNextItem

interface SyncRepository {
    suspend fun getUpNext(): SnacResult<List<UpNextItem>>

    suspend fun getPlayback(): SnacResult<List<PlaybackItem>>
}
