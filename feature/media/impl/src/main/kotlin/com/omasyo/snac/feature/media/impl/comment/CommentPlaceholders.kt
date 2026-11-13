package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun CommentCardPlaceholder(
    seed: Int,
    modifier: Modifier = Modifier,
) {
    val hasReplies = seed % 2 == 0
    val replyCount = 1 + seed % 3

    Column(modifier) {
        CommentHeaderPlaceholder()
        if (hasReplies) {
            ReplyPreviews { ReplyCardPlaceholders(replyCount) }
        }
    }
}

@Composable
internal fun ReplyCardPlaceholders(count: Int) {
    Column { repeat(count) { ReplyCardPlaceholder() } }
}

@Composable
internal fun ReplyCardPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .replyCardFrame(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarPlaceholder()
            Bar(Modifier.size(72.dp, 10.dp))
            Bar(Modifier.size(32.dp, 8.dp))
        }
        TextLine(Modifier.fillMaxWidth())
        Bar(Modifier.size(160.dp, 12.dp))
        Bar(
            Modifier
                .padding(vertical = 4.dp)
                .size(32.dp, 10.dp)
        )
    }
}

@Composable
private fun CommentHeaderPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarPlaceholder()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Bar(Modifier.size(72.dp, 10.dp))
                Bar(Modifier.size(40.dp, 8.dp))
            }
            Bar(Modifier.size(12.dp, 16.dp))
        }
        repeat(2) { TextLine(Modifier.fillMaxWidth()) }
        Bar(
            Modifier
                .padding(top = 2.dp, bottom = 12.dp)
                .size(160.dp, 12.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(2) {
                Bar(
                    Modifier
                        .padding(vertical = 8.dp)
                        .size(40.dp, 10.dp)
                )
            }
        }
    }
}

@Composable
private fun AvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .border(2.dp, SnacColors.outline, CircleShape)
            .clip(CircleShape)
            .background(imageGradient())
    )
}

@Composable
private fun TextLine(modifier: Modifier) {
    Bar(
        Modifier
            .padding(vertical = 2.dp)
            .then(modifier)
            .height(12.dp)
    )
}

@Composable
private fun Bar(modifier: Modifier) {
    Box(modifier.background(SnacColors.placeholder))
}

@Preview
@Composable
private fun CommentCardPlaceholderPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        ) {
            CommentCardPlaceholder(seed = 2)
        }
    }
}
