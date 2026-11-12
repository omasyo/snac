package com.omasyo.snac.feature.media.impl.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.formatCount
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.core.ui.R as UiR

@Composable
internal fun CommentStats(comment: Comment) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CommentStat(
            icon = UiR.drawable.heart_bold,
            count = comment.likesCount,
            description = pluralStringResource(
                R.plurals.likes,
                comment.likesCount,
                comment.likesCount.formatCount(),
            ),
        )
        CommentStat(
            icon = UiR.drawable.chat_circle_bold,
            count = comment.repliesCount,
            description = pluralStringResource(
                R.plurals.replies,
                comment.repliesCount,
                comment.repliesCount.formatCount(),
            ),
        )
    }
}

@Composable
internal fun CommentStat(
    @DrawableRes icon: Int,
    count: Int,
    description: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        Icon(
            icon = icon,
            contentDescription = null,
            tint = SnacColors.muted,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = count.formatCount(),
            style = SnacTypography.overline,
            color = SnacColors.muted
        )
    }
}

@Composable
fun CommentPreviewCard(
    comment: Comment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(comment) { mutableStateOf(comment.isSpoiler) }
    Column(
        modifier = modifier
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
            Image(
                imageUrl = comment.user.imageUrl,
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, SnacColors.outline, CircleShape)
                    .clip(CircleShape)
            )
            Text(
                text = comment.user.name,
                style = SnacTypography.label,
                modifier = Modifier.weight(1f),
            )
            if (comment.isReview) {
                Text(
                    text = "REVIEW",
                    style = SnacTypography.overline,
                    color = SnacColors.accent,
                    modifier = Modifier
                        .background(SnacColors.film)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            comment.rating?.let {
                Text(
                    text = it.toString(),
                    style = SnacTypography.subtitle,
                    color = SnacColors.signal,
                )
            }
        }
        Text(
            text = comment.comment,
            style = SnacTypography.caption,
            maxLines = 3,
            modifier = if (blurred) Modifier.blur(8.dp, BlurredEdgeTreatment.Unbounded)
            else Modifier
        )
        CommentStats(comment)
    }
}

@Composable
fun ReviewPreviewPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .silhouette(offset = DpOffset(4.dp, 4.dp))
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, SnacColors.outline, CircleShape)
                    .clip(CircleShape)
                    .background(imageGradient())
            )

            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(10.dp)
                    .background(SnacColors.placeholder)
            )
            Spacer(Modifier.weight(1f))

            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(12.dp)
                    .height(16.dp)
                    .background(SnacColors.placeholder)
            )
        }

        repeat(2) {
            Box(
                Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(SnacColors.placeholder)
            )
        }
        Box(
            Modifier
                .width(160.dp)
                .height(12.dp)
                .background(SnacColors.placeholder)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(10.dp)
                    .background(SnacColors.placeholder)
            )
            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(10.dp)
                    .background(SnacColors.placeholder)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewCardPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentPreviewCard(
                comment = Comment.PreviewSample.first().copy(isSpoiler = true),
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCardPlaceholderPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            ReviewPreviewPlaceholder()
        }
    }
}