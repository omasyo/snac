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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.R as UiR
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.formatCount
import com.omasyo.snac.core.ui.format.formatRelative
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import java.time.LocalDateTime

data class User(
    val slug: String,
    val name: String,
    val imageUrl: String?,
)

data class Review(
    val id: Long,
    val comment: String,
    val isSpoiler: Boolean,
    val isReview: Boolean,
    val gifUrl: String?,
    val repliesCount: Int,
    val likesCount: Int,
    val rating: Int,
    val user: User,
    val createdAt: LocalDateTime,
) {
    companion object {
        val PreviewSample = List(20) {
            Review(
                id = it.toLong(),
                comment = "Nice music but a weird and trashy movie. In theory, it is a horror movie, but come on, it is far from that. Don’t watch it!",
                isSpoiler = it % 3 == 0,
                isReview = it % 2 == 0,
                gifUrl = "",
                repliesCount = 20,
                likesCount = 458,
                rating = 7,
                user = User(
                    slug = "user",
                    name = "omasyo",
                    imageUrl = null
                ),
                createdAt = LocalDateTime.now().minusDays(6),
            )
        }
    }
}

@Composable
fun ReviewCard(
    review: Review,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(review) { mutableStateOf(review.isSpoiler) }
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
                imageUrl = review.user.imageUrl,
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, SnacColors.outline, CircleShape)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.user.name,
                    style = SnacTypography.label,
                )
                Text(
                    text = review.createdAt.formatRelative(),
                    style = SnacTypography.caption,
                    color = SnacColors.muted
                )
            }
            if (review.isReview) {
                Text(
                    text = "REVIEW",
                    style = SnacTypography.overline,
                    color = SnacColors.accent,
                    modifier = Modifier
                        .background(SnacColors.film)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Text(
                text = review.rating.toString(),
                style = SnacTypography.subtitle,
                color = SnacColors.signal,
            )
        }
        Box {
            Text(
                text = review.comment,
                style = SnacTypography.body,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        enabled = review.isSpoiler,
                    ) {
                        blurred = !blurred

                    }
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
                    modifier = modifier
                        .rotate(-12f)
                        .align(BiasAlignment(0.8f, 0f))
                        .border(2.dp, SnacColors.signal)
                        .background(SnacColors.chip)
                        .padding(horizontal = 8.dp)
                )
            }
        }
        ReviewStats(review)
    }
}

@Composable
private fun ReviewStats(review: Review) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ReviewStat(
            icon = UiR.drawable.heart_bold,
            count = review.likesCount,
            description = pluralStringResource(
                R.plurals.likes,
                review.likesCount,
                review.likesCount.formatCount(),
            ),
        )
        ReviewStat(
            icon = UiR.drawable.chat_circle_bold,
            count = review.repliesCount,
            description = pluralStringResource(
                R.plurals.replies,
                review.repliesCount,
                review.repliesCount.formatCount(),
            ),
        )
    }
}

@Composable
private fun ReviewStat(
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
fun ReviewPreviewCard(
    review: Review,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var blurred by rememberSaveable(review) { mutableStateOf(review.isSpoiler) }
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
                imageUrl = review.user.imageUrl,
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, SnacColors.outline, CircleShape)
                    .clip(CircleShape)
            )
            Text(
                text = review.user.name,
                style = SnacTypography.label,
                modifier = Modifier.weight(1f),
            )
            if (review.isReview) {
                Text(
                    text = "REVIEW",
                    style = SnacTypography.overline,
                    color = SnacColors.accent,
                    modifier = Modifier
                        .background(SnacColors.film)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Text(
                text = review.rating.toString(),
                style = SnacTypography.subtitle,
                color = SnacColors.signal,
            )
        }
        Text(
            text = review.comment,
            style = SnacTypography.caption,
            modifier = if (blurred) Modifier.blur(8.dp, BlurredEdgeTreatment.Unbounded)
            else Modifier
        )
        ReviewStats(review)
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
private fun Preview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            ReviewCard(
                review = Review.PreviewSample.first().copy(isSpoiler = true),
                onClick = {},
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
            ReviewPreviewCard(
                review = Review.PreviewSample.first().copy(isSpoiler = true),
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