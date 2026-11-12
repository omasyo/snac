package com.omasyo.snac.core.data.comment

import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Reply
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun getTopReplies(id: Long): SnacResult<List<Reply>>

    fun getReplies(id: Long): Flow<PagingData<Reply>>
}
