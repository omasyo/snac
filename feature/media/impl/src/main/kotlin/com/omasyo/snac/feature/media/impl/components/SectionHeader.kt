package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.TintIndication
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
            val tintColor = SnacColors.page
            Text(
                text = "SEE ALL →",
                style = SnacTypography.label,
                color = SnacColors.signal,
                modifier = Modifier
                    .alignByBaseline()
                    .clickable(
                        interactionSource = null,
                        indication = TintIndication(
                            contentOnly = true,
                            focusOpacity = 0.2f,
                            pressOpacity = 0.5f
                        ) { tintColor },
                        onClick = onSeeAllClick,
                    ),
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
