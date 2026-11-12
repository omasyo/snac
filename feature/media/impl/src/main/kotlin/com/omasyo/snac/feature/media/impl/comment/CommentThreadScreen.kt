package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun CommentThreadScreen(
    comment: Comment,
    replies: List<Reply>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommentThread(
        comment = comment,
        replies = replies,
        onCollapse = onBackClick,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    )
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        CommentThreadScreen(
            comment = Comment.PreviewSample.first(),
            replies = Reply.PreviewSample,
            onBackClick = {},
            modifier = Modifier.background(SnacColors.page),
        )
    }
}
