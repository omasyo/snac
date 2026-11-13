package com.omasyo.snac.feature.person.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.ui.components.ArrowLink
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.core.ui.R as UiR

@Composable
internal fun BiographyCard(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .silhouette(offset = DpOffset(6.dp, 6.dp))
            .border(4.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.biography).uppercase(),
            style = SnacTypography.title,
            color = SnacColors.ink,
        )
        when (state) {
            is UiState.Success -> {
                var expanded by rememberSaveable { mutableStateOf(false) }
                var hasOverflow by remember { mutableStateOf(false) }

                Text(
                    text = state.content,
                    style = SnacTypography.body,
                    color = SnacColors.ink,
                    maxLines = if (expanded) Int.MAX_VALUE else CollapsedLines,
                    onTextLayout = { hasOverflow = it.hasVisualOverflow },
                )
                if (hasOverflow) {
                    ArrowLink(
                        text = stringResource(UiR.string.read_more),
                        onClick = { expanded = true },
                    )
                }
            }

            else -> {
                repeat(CollapsedLines - 1) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(SnacColors.placeholder)
                    )
                }
                Box(
                    Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .background(SnacColors.placeholder)
                )
            }
        }
    }
}

private const val CollapsedLines = 6

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            BiographyCard(state = UiState.Success(PersonDetails.PreviewSample.biography.orEmpty()))
        }
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            BiographyCard(state = UiState.Loading)
        }
    }
}
