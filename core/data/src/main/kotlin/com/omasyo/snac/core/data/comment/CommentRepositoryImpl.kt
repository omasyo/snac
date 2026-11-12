package com.omasyo.snac.core.data.comment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toReply
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.data.paging.CustomPagingSource
import com.omasyo.snac.core.network.comment.CommentNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
@Inject
class CommentRepositoryImpl(
    private val commentNetworkSource: CommentNetworkSource,
) : CommentRepository {
    override suspend fun getTopReplies(id: Long): SnacResult<List<Reply>> =
        commentNetworkSource.getReplies(id, page = 1, limit = 2)
            .map { list -> list.map { it.toReply() } }

    override fun getReplies(id: Long): Flow<PagingData<Reply>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> commentNetworkSource.getReplies(id, page, PAGE_SIZE) },
                mapper = { it.toReply() },
            )
        }.flow

    companion object {
        private const val PAGE_SIZE = 30

        private val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            jumpThreshold = PAGE_SIZE * 3,
        )
    }
}
