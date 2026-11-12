package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.NavDisplay
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

internal val CommentThreadTransitionMetadata =
    NavDisplay.transitionSpec {
        EnterTransition.None togetherWith fadeOut()
    } + NavDisplay.popTransitionSpec {
        fadeIn() togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.predictivePopTransitionSpec {
        fadeIn() togetherWith ExitTransition.KeepUntilTransitionsFinished
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
