package com.omasyo.snac.core.network.comment

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.comment.models.ReplyResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class CommentNetworkSourceImpl(
    private val client: HttpClient
) : CommentNetworkSource {
    override suspend fun getReplies(
        id: Long,
        page: Int,
        limit: Int,
    ): SnacResult<List<ReplyResponse>> = safeCall {
        client.get("comments/$id/replies") {
            parameter("extended", "full")
            parameter("page", page)
            parameter("limit", limit)
        }
    }
}
