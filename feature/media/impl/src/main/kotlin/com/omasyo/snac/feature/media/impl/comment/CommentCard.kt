package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.format.formatCount
import com.omasyo.snac.core.ui.format.formatRelative
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.navigation.navSharedBounds
import com.omasyo.snac.core.ui.navigation.navSharedElement
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.components.CommentStat
import com.omasyo.snac.feature.media.impl.components.CommentStats
import com.omasyo.snac.core.ui.R as UiR

@Composable
internal fun CommentCard(
    comment: Comment,
    replies: List<Reply>,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        CommentHeader(
            comment = comment,
            onClick = onExpand,
            modifier = Modifier.sharedHeader(comment),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .sharedReplySection(comment)
                .replySection()
                .clipToBounds()
                .padding(vertical = 12.dp),
        ) {
            replies.take(2).forEach { reply ->
                ReplyCard(
                    reply = reply,
                    modifier = Modifier.sharedReply(comment, reply),
                )
            }
            ViewAllReplies(
                count = comment.repliesCount,
                onClick = onExpand,
            )
        }
    }
}

@Composable
internal fun CommentThread(
    comment: Comment,
    replies: List<Reply>,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        CommentHeader(
            comment = comment,
            onClick = onCollapse,
            modifier = Modifier.sharedHeader(comment),
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .sharedReplySection(comment)
                .replySection(),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(replies, key = { it.id }) { reply ->
                ReplyCard(
                    reply = reply,
                    modifier = Modifier.sharedReply(comment, reply),
                )
            }
        }
    }
}

@Composable
private fun Modifier.sharedHeader(comment: Comment): Modifier =
    navSharedElement(key = "comment-header-${comment.id}", zIndexInOverlay = 2f)

@Composable
private fun Modifier.sharedReplySection(comment: Comment): Modifier =
    navSharedBounds(key = "comment-replies-${comment.id}")

@Composable
private fun Modifier.sharedReply(comment: Comment, reply: Reply): Modifier =
    navSharedElement(key = "comment-reply-${comment.id}-${reply.id}", zIndexInOverlay = 1f)

@Composable
private fun Modifier.replySection(): Modifier = this
    .silhouette()
    .padding(start = 20.dp)
    .border(2.dp, SnacColors.outline)
    .background(SnacColors.chip)

@Composable
private fun CommentHeader(
    comment: Comment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dashColor = SnacColors.rule
    Column(
        modifier = modifier
            .zIndex(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = SilhouettePressIndication(offset = DpOffset(4.dp, 4.dp)),
                onClick = onClick,
            )
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(comment.user.imageUrl)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comment.user.name,
                    style = SnacTypography.label,
                )
                Text(
                    text = comment.createdAt.formatRelative(),
                    style = SnacTypography.caption,
                    color = SnacColors.muted
                )
            }
            ReviewBadges(isReview = comment.isReview, rating = comment.rating)
        }
        SpoilerText(
            text = comment.comment,
            isSpoiler = comment.isSpoiler,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .drawBehind { drawDashedBottomRule(dashColor) }
                .padding(bottom = 8.dp),
        )
        CommentStats(comment)
    }
}

@Composable
private fun ReplyCard(
    reply: Reply,
    modifier: Modifier = Modifier,
) {
    val dashColor = SnacColors.rule
    Column(
        modifier = modifier
            .background(SnacColors.ink)
            .film(
                side = Side.Start,
                holeLength = 8.dp,
                holeDepth = 10.dp,
                holeGap = 8.dp,
            )
            .background(SnacColors.chip)
            .padding(bottom = 12.dp)
            .drawBehind { drawDashedBottomRule(dashColor) }
            .padding(bottom = 12.dp)
            .padding(start = 24.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(reply.user.imageUrl)
            Text(
                text = reply.user.name,
                style = SnacTypography.label,
                maxLines = 1,
                modifier = Modifier.weight(1f, false)
            )
            Text(
                text = reply.createdAt.formatRelative(),
                style = SnacTypography.caption,
                color = SnacColors.muted
            )
            Spacer(Modifier.weight(1f))
            ReviewBadges(isReview = reply.isReview, rating = reply.rating)
        }
        SpoilerText(text = reply.comment, isSpoiler = reply.isSpoiler)
        CommentStat(
            icon = UiR.drawable.heart_bold,
            count = reply.likesCount,
            description = pluralStringResource(
                R.plurals.likes,
                reply.likesCount,
                reply.likesCount.formatCount(),
            ),
        )
    }
}

@Composable
private fun ViewAllReplies(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tintColor = SnacColors.page
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 24.dp, end = 12.dp)
            .clickable(
                interactionSource = null,
                indication = TintIndication(
                    contentOnly = true,
                    focusOpacity = 0.2f,
                    pressOpacity = 0.5f
                ) { tintColor },
                onClick = onClick,
            ),
    ) {
        Text(
            text = "VIEW ALL ${count.formatCount()} REPLIES",
            style = SnacTypography.tag,
            color = SnacColors.signal,
        )
        Icon(
            icon = UiR.drawable.caret_right_bold,
            contentDescription = null,
            tint = SnacColors.signal,
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
private fun Avatar(imageUrl: String?) {
    Image(
        imageUrl = imageUrl,
        modifier = Modifier
            .size(32.dp)
            .border(2.dp, SnacColors.outline, CircleShape)
            .clip(CircleShape)
    )
}

@Composable
private fun ReviewBadges(isReview: Boolean, rating: Int?) {
    if (isReview) {
        Text(
            text = "REVIEW",
            style = SnacTypography.overline,
            color = SnacColors.accent,
            modifier = Modifier
                .background(SnacColors.film)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
    rating?.let {
        Text(
            text = it.toString(),
            style = SnacTypography.subtitle,
            color = SnacColors.signal,
        )
    }
}

@Composable
private fun SpoilerText(
    text: String,
    isSpoiler: Boolean,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(text, isSpoiler) { mutableStateOf(isSpoiler) }
    Box(modifier) {
        Text(
            text = text,
            style = SnacTypography.body,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = null,
                    enabled = isSpoiler,
                ) { blurred = !blurred }
                .then(
                    if (blurred) Modifier.blur(8.dp, BlurredEdgeTreatment.Unbounded)
                    else Modifier
                )
        )
        if (blurred) {
            Text(
                text = "SPOILER",
                style = SnacTypography.caption,
                color = SnacColors.signal,
                modifier = Modifier
                    .rotate(-12f)
                    .align(BiasAlignment(0.8f, 0f))
                    .border(2.dp, SnacColors.signal)
                    .background(SnacColors.chip)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

private fun DrawScope.drawDashedBottomRule(color: Color) {
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(4.dp.toPx(), 2.dp.toPx())
        )
    )
}

@Preview
@Composable
private fun CommentCardPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentCard(
                comment = Comment.PreviewSample.first().copy(isSpoiler = false),
                replies = Reply.PreviewSample,
                onExpand = {},
            )
        }
    }
}

@Preview
@Composable
private fun CommentThreadPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentThread(
                comment = Comment.PreviewSample.first().copy(isSpoiler = false),
                replies = Reply.PreviewSample,
                onCollapse = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
