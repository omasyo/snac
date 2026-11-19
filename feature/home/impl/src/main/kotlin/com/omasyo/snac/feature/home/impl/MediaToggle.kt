package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

internal enum class MediaTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
internal fun MediaToggle(
    selectedTab: MediaTab,
    onTabClick: (MediaTab) -> Unit,
    color: Color,
    backgroundColor: Color,
    selectedColor: Color,
    selectedContentColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.border(2.dp, color)
    ) {
        for (tab in MediaTab.entries) {
            val selected = tab == selectedTab
            val indicationColor = SnacColors.accent
            Text(
                text = tab.label,
                color = if (selected) selectedContentColor else color,
                modifier = Modifier
                    .background(if (selected) selectedColor else backgroundColor)
                    .clickable(
                        interactionSource = null,
                        indication = TintIndication { indicationColor },
                        onClick = { onTabClick(tab) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var selectedTab by remember { mutableStateOf(MediaTab.Movie) }
    SnacTheme {
        MediaToggle(
            selectedTab = selectedTab,
            onTabClick = { selectedTab = it },
            color = SnacColors.ink,
            backgroundColor = SnacColors.card,
            selectedColor = SnacColors.ink,
            selectedContentColor = SnacColors.accent,
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        )
    }
}
