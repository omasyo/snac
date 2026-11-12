package com.omasyo.snac.core.network.comment

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.comment.models.ReplyResponse

interface CommentNetworkSource : NetworkSource {
    suspend fun getReplies(id: Long, page: Int, limit: Int): SnacResult<List<ReplyResponse>>
}
