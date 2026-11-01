package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.ArrowLink
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null,
) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = SnacTypography.title,
            color = SnacColors.ink,
            modifier = Modifier
                .weight(1f)
                .alignByBaseline()
                .silhouette(SnacColors.accent),
        )
        if (onSeeAllClick != null) {
            ArrowLink(
                text = "SEE ALL",
                onClick = onSeeAllClick,
                modifier = Modifier.alignByBaseline(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        SectionHeader(
            title = "CAST",
            onSeeAllClick = {},
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        )
    }
}
