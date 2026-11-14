package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.format.formatCount
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.format.formatRelative
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.navigation.navSharedElement
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.components.CommentStat
import com.omasyo.snac.feature.media.impl.components.CommentStats
import kotlinx.coroutines.flow.flowOf
import kotlin.math.min
import com.omasyo.snac.core.ui.R as UiR

private const val PREVIEW_REPLIES = 2
private const val COLLAPSED_LINES = 3
private val MAX_HEADER_HEIGHT = 360.dp

@Composable
internal fun CommentCard(
    comment: Comment,
    replies: UiState<List<Reply>>,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        CommentHeader(
            comment = comment,
            onExpandThread = {
                if (comment.repliesCount > 0) {
                    onExpand()
                }
            },
            modifier = Modifier.sharedHeader(comment),
        )
        if (comment.repliesCount > 0) {
            ReplyPreviews {
                val previewCount = min(comment.repliesCount, PREVIEW_REPLIES)
                when (replies) {
                    UiState.Loading -> ReplyCardPlaceholders(previewCount)

                    is UiState.Error -> ErrorBox(
                        description = replies.error.message,
                        onRetry = replies.refresh,
                    ) {
                        ReplyCardPlaceholders(previewCount)
                    }

                    is UiState.Success -> replies.content.take(PREVIEW_REPLIES).forEach { reply ->
                        ReplyCard(reply = reply, onExpandThread = onExpand)
                    }
                }
                ViewAllReplies(
                    count = comment.repliesCount,
                    onClick = onExpand,
                )
            }
        }
    }
}

@Composable
internal fun CommentThread(
    comment: Comment,
    replies: LazyPagingItems<Reply>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        CommentHeader(
            comment = comment,
            onExpandThread = null,
            modifier = Modifier.sharedHeader(comment),
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .replySection(),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            val placeholderCount = comment.repliesCount.coerceIn(1, 6)
            val isEmpty = replies.itemCount == 0

            when (val refreshState = replies.loadState.refresh) {
                is LoadState.Loading -> {
                    if (isEmpty) {
                        item("refresh-loading") { ReplyCardPlaceholders(placeholderCount) }
                    }
                }

                is LoadState.Error -> {
                    if (isEmpty) {
                        item("refresh-error") {
                            ErrorBox(
                                description = refreshState.error.message
                                    ?: SnacResult.Error.Unexpected.message,
                                onRetry = replies::retry,
                            ) {
                                ReplyCardPlaceholders(placeholderCount)
                            }
                        }
                    }
                }

                is LoadState.NotLoading -> {}
            }

            items(
                count = replies.itemCount,
                key = replies.itemKey { it.id },
            ) { index ->
                val reply = replies[index]
                if (reply != null) {
                    ReplyCard(reply = reply, onExpandThread = null)
                } else {
                    ReplyCardPlaceholder()
                }
            }
            when (replies.loadState.append) {
                is LoadState.Loading -> item("append-loading") { ReplyCardPlaceholder() }
                is LoadState.Error -> item("append-error") {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    ) {
                        Button(text = "Retry", onClick = replies::retry)
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Composable
private fun Modifier.sharedHeader(comment: Comment): Modifier =
    navSharedElement(key = "comment-header-${comment.id}", zIndexInOverlay = 2f)

@Composable
private fun Modifier.replySection(): Modifier = this
    .silhouette()
    .padding(start = 20.dp)
    .border(2.dp, SnacColors.outline)
    .background(SnacColors.chip)

@Composable
internal fun ReplyPreviews(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .replySection()
            .clipToBounds()
            .padding(vertical = 12.dp),
        content = content,
    )
}

@Composable
internal fun Modifier.replyCardFrame(): Modifier {
    val dashColor = SnacColors.rule
    return this
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
        .padding(start = 24.dp, end = 12.dp)
}

@Composable
private fun CommentHeader(
    comment: Comment,
    onExpandThread: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(comment.isSpoiler) { mutableStateOf(comment.isSpoiler) } //TODO store this at a much higher level, might even get to the DB if we later add one
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val dashColor = SnacColors.rule

    Column(
        modifier = modifier
            .zIndex(1f)
            .heightIn(max = MAX_HEADER_HEIGHT)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = SilhouettePressIndication(offset = DpOffset(4.dp, 4.dp)),
                onClick = onExpandThread ?: { if (comment.isSpoiler) blurred = !blurred },
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
            blurred = blurred,
            expanded = expanded,
            onToggleExpanded = onExpandThread ?: { expanded = !expanded },
            modifier = Modifier
                .weight(1f, false)
                .then(if (onExpandThread == null) Modifier.verticalScroll(scrollState) else Modifier)
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
    onExpandThread: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(reply.isSpoiler) { mutableStateOf(reply.isSpoiler) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.replyCardFrame(),
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
        SpoilerText(
            text = reply.comment,
            blurred = blurred,
            expanded = expanded,
            onToggleExpanded = onExpandThread ?: { expanded = !expanded },
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = null,
                enabled = reply.isSpoiler,
                onClick = onExpandThread ?: { blurred = !blurred },
            ),
        )
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
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 24.dp, end = 12.dp)
            .linkClickable(onClick),
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
    blurred: Boolean,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var hasOverflow by remember { mutableStateOf(false) }
    Column(modifier) {
        Box {
            Text(
                text = text,
                style = SnacTypography.body,
                maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_LINES,
                onTextLayout = { hasOverflow = it.hasVisualOverflow },
                modifier = Modifier.then(
                    if (blurred) Modifier.blur(8.dp, BlurredEdgeTreatment.Unbounded)
                    else Modifier
                ),
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
        if (hasOverflow || expanded) {
            ReadMoreToggle(expanded = expanded, onClick = onToggleExpanded)
        }
    }
}

@Composable
private fun ReadMoreToggle(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(if (expanded) UiR.string.show_less else UiR.string.read_more),
        style = SnacTypography.label,
        color = SnacColors.signal,
        modifier = modifier.linkClickable(onClick),
    )
}

@Composable
private fun Modifier.linkClickable(onClick: () -> Unit): Modifier {
    val tintColor = SnacColors.page
    return clickable(
        interactionSource = null,
        indication = TintIndication(
            contentOnly = true,
            focusOpacity = 0.2f,
            pressOpacity = 0.5f,
        ) { tintColor },
        onClick = onClick,
    )
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
                comment = Comment.PreviewSample.first().copy(isSpoiler = true),
                replies = UiState.Success(Reply.PreviewSample),
                onExpand = {},
            )
        }
    }
}

@Preview
@Composable
private fun CommentCardLoadingPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentCard(
                comment = Comment.PreviewSample.first(),
                replies = UiState.Loading,
                onExpand = {},
            )
        }
    }
}

@Preview
@Composable
private fun CommentThreadPreview() {
    val replies =
        remember { flowOf(PagingData.from(Reply.PreviewSample)) }.collectAsLazyPagingItems()
    SnacTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentThread(
                comment = Comment.PreviewSample.first().copy(isSpoiler = false),
                replies = replies,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
private fun CommentThreadLoadingPreview() {
    val loading =
        LoadStates(LoadState.Loading, LoadState.NotLoading(false), LoadState.NotLoading(false))
    val replies = remember { flowOf(PagingData.empty<Reply>(loading)) }.collectAsLazyPagingItems()
    SnacTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentThread(
                comment = Comment.PreviewSample.first().copy(isSpoiler = false),
                replies = replies,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
