package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun GenreChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = SnacTypography.tag,
        modifier = modifier
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.chip)
            .padding(12.dp, 2.dp)
    )
}

@Composable
fun GenreChipPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.chip)
            .padding(12.dp, 2.dp)
            .padding(vertical = 3.dp)
            .size(48.dp, 16.dp)
    )
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        GenreChip("Drama", Modifier.padding(8.dp))
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        GenreChipPlaceholder(Modifier.padding(8.dp))
    }
}