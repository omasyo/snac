package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.SnacError
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Stamp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@Composable
internal fun AnticipatedSection(
    onMediaClick: (Media) -> Unit,
    onAllChartsClick: () -> Unit,
    state: UiState<List<AnticipatedItem>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "ANTICIPATED",
                style = SnacTypography.title,
                color = SnacColors.ink,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
                    .silhouette(SnacColors.accent),
            )
            val tintColor = SnacColors.page
            Text(
                text = "ALL CHARTS →",
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
                        onClick = onAllChartsClick,
                    ),
            )
        }

        val placeholder =
            remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable) } }

        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    placeholder(false)
                }
            }


            UiState.Loading -> placeholder(true)

            is UiState.Success -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.content, key = { it.media.slug }) { item ->
                        AnticipatedCard(
                            title = item.media.title,
                            listCount = item.listCount,
                            imageUrl = item.media.imageUrl,
                            onClick = { onMediaClick(item.media) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnticipatedCard(
    title: String,
    listCount: Int,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
    ) {
        Box(
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(2.dp, SnacColors.ink)
                .size(224.dp, 128.dp)
        ) {
            Image(
                imageUrl = imageUrl,
                modifier = Modifier.matchParentSize(),
            )
            Stamp(
                text = "in $listCount lists", //TODO should have comma seperator
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .rotate(-4f)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
            )
        }
        val tintColor = SnacColors.page
        Text(
            text = title,
            style = SnacTypography.subtitle,
            modifier = Modifier
                .indication(
                    interactionSource, TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f,
                    ) { tintColor })
        )
    }
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(4) {
            Column {
                Box(
                    modifier = Modifier
                        .silhouette()
                        .border(2.dp, SnacColors.ink)
                        .background(imageGradient())
                        .size(224.dp, 128.dp)
                )
                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .width(120.dp)
                        .height(16.dp)
                        .background(SnacColors.faint)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            AnticipatedSection(
                onMediaClick = { },
                onAllChartsClick = { },
                state = UiState.Success(AnticipatedItem.PreviewSample),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<AnticipatedItem>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacError.ErrorOnMyPart, {})
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            AnticipatedSection(
                onMediaClick = { },
                onAllChartsClick = { },
                state = state,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}