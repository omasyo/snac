package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@Composable
internal fun BoxOfficeSection(
    onMovieClick: (Movie) -> Unit,
    state: UiState<List<BoxOfficeItem>>,
    modifier: Modifier = Modifier,
) {

    val loadingPlaceholder =
        remember {
            movableContentOf {
                BoxOfficeSectionLayout(modifier) {
                    repeat(5) {
                        PlaceholderEntry(it, Modifier)
                    }
                }
            }
        }

    when (state) {
        is UiState.Error -> {
            ErrorBox(
                description = state.error.message,
                onRetry = state.refresh,
            ) {
                loadingPlaceholder()
            }
        }

        UiState.Loading -> loadingPlaceholder()

        is UiState.Success -> {
            BoxOfficeSectionLayout(modifier) {
                state.content.forEachIndexed { index, item ->
                    Entry(
                        position = index + 1,
                        title = item.media.title,
                        revenue = formatRevenue(item.revenue),
                        onClick = { onMovieClick(item.media) },
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Composable
internal fun BoxOfficeSectionLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .silhouette(SnacColors.accent, DpOffset(6.dp, 6.dp))
            .background(SnacColors.film)
            .padding(16.dp),
    ) {
        Row {
            Text(
                text = "BOX OFFICE",
                style = SnacTypography.title,
                color = SnacColors.accent,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            )
            Text(
                text = "Revenue",
                style = SnacTypography.hand,
                color = SnacColors.onFilm,
                modifier = Modifier.alignByBaseline()
            )
        }
        content()
    }
}

private fun formatRevenue(dollars: Long): String = when { //TODO who asked for this?
    dollars >= 1_000_000 -> "$%.1fM".format(dollars / 1_000_000.0)
    dollars >= 1_000 -> "$%.1fK".format(dollars / 1_000.0)
    else -> "$$dollars"
}

@Composable
private fun Entry(
    position: Int,
    title: String,
    revenue: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val indicationColor = SnacColors.accent

    EntryLayout(
        position = {
            val positionColor = when (position) {
                1 -> SnacColors.signal
                2, 3 -> SnacColors.onFilm
                else -> SnacColors.faint
            }
            Text(
                text = "$position",
                style = SnacTypography.title,
                color = positionColor,
            )
        },
        title = {
            Text(
                text = title,
                style = SnacTypography.label,
                color = SnacColors.onFilm,
            )
        },
        revenue = {
            Text(
                text = revenue,
                style = SnacTypography.subtitle,
                color = SnacColors.accent,
            )
        },
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
    )
}

@Composable
private fun PlaceholderEntry(position: Int, modifier: Modifier) {
    EntryLayout(
        position = {
            Text(
                text = "$position",
                style = SnacTypography.title,
                color = SnacColors.faint,
            )
        },
        title = {
            Box(
                Modifier
                    .height(16.dp)
                    .width(144.dp)
                    .background(SnacColors.onFilm.copy(0.2f))
            )
        },
        revenue = {

            Box(
                Modifier
                    .height(16.dp)
                    .width(56.dp)
                    .background(SnacColors.accent.copy(0.2f))
            )
//            Text(
//                text = revenue,
//                style = SnacTypography.subtitle,
//                color = SnacColors.accent,
//            )
        },
        modifier = modifier,
    )
}


@Composable
private fun EntryLayout(
    position: @Composable () -> Unit,
    title: @Composable () -> Unit,
    revenue: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 2.dp.toPx()))
                )
            },
    ) {
        Box(Modifier.widthIn(44.dp)) { position() }
        Box(Modifier.weight(1f)) { title() }
        Box { revenue() }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        BoxOfficeSection(
            onMovieClick = {},
            state = UiState.Success(BoxOfficeItem.PreviewSample),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    SnacTheme {
        BoxOfficeSection(
            onMovieClick = {},
            state = UiState.Loading,
            modifier = Modifier.padding(16.dp),
        )
    }
}


@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<BoxOfficeItem>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Success(BoxOfficeItem.PreviewSample)
    }

    SnacTheme {
        BoxOfficeSection(
            onMovieClick = {},
            state = state,
            modifier = Modifier.padding(16.dp),
        )
    }
}