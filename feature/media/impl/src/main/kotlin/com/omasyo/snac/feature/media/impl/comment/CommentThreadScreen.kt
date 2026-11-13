package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.api.navigation.CommentThreadNavKey
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun CommentThreadScreen(
    key: CommentThreadNavKey,
    modifier: Modifier = Modifier,
    viewModel: CommentThreadViewModel =
        assistedMetroViewModel<CommentThreadViewModel, CommentThreadViewModel.Factory> {
            create(key.id)
        },
) {
    CommentThreadScreen(
        comment = remember(key) { key.toComment() },
        replies = viewModel.replies.collectAsLazyPagingItems(),
        modifier = modifier,
    )
}

@Composable
internal fun CommentThreadScreen(
    comment: Comment,
    replies: LazyPagingItems<Reply>,
    modifier: Modifier = Modifier,
) {
    CommentThread(
        comment = comment,
        replies = replies,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    )
}

@Preview
@Composable
private fun Preview() {
    val replies =
        remember { flowOf(PagingData.from(Reply.PreviewSample)) }.collectAsLazyPagingItems()
    SnacTheme {
        CommentThreadScreen(
            comment = Comment.PreviewSample.first(),
            replies = replies,
            modifier = Modifier.background(SnacColors.page),
        )
    }
}
