package com.omasyo.snac.feature.calendar.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.omasyo.snac.core.model.media.CalendarMediaType
import com.omasyo.snac.core.model.media.Genre
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun CalendarFilterSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    mediaType: CalendarMediaType,
    onMediaTypeChange: (CalendarMediaType) -> Unit,
    genre: Genre,
    genres: List<Genre>,
    onGenreChange: (Genre) -> Unit,
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = visible

    // Keep the dialog around until the exit animation finishes.
    if (!visibleState.currentState && !visibleState.targetState) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        // The scrim below fades with the sheet, so drop the window's instant dim.
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { window?.setDimAmount(0f) }

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(SnacColors.shadow.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onDismiss,
                    )
                    .statusBarsPadding(),
            ) {
                CalendarFilters(
                    mediaType = mediaType,
                    onMediaTypeChange = onMediaTypeChange,
                    genre = genre,
                    genres = genres,
                    onGenreChange = onGenreChange,
                    modifier = Modifier
                        .animateEnterExit(
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it },
                        )
                        // Swallow taps so they don't reach the scrim.
                        .clickable(interactionSource = null, indication = null) {},
                )
            }
        }
    }
}

@Composable
private fun CalendarFilters(
    mediaType: CalendarMediaType,
    onMediaTypeChange: (CalendarMediaType) -> Unit,
    genre: Genre,
    genres: List<Genre>,
    onGenreChange: (Genre) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = SnacColors.outline
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SnacColors.card)
            .drawBehind {
                drawLine(outline, Offset.Zero, Offset(size.width, 0f), 2.dp.toPx())
            }
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp),
    ) {
        Text(text = "Type".uppercase(), style = SnacTypography.subtitle)
        TabRow(
            selectedTab = mediaType,
            onSelectedTabChange = onMediaTypeChange,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "Genre".uppercase(),
            style = SnacTypography.subtitle,
            modifier = Modifier.padding(top = 24.dp),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp),
        ) {
            genres.forEach {
                GenreOption(
                    label = it.name,
                    selected = it == genre,
                    onClick = { onGenreChange(it) },
                )
            }
        }
    }
}

@Composable
private fun GenreOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = SnacTypography.tag,
        color = if (selected) SnacColors.onAccent else SnacColors.ink,
        modifier = modifier
            .clickable(
                indication = remember { SilhouettePressIndication() },
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .border(2.dp, SnacColors.outline)
            .background(if (selected) SnacColors.accent else SnacColors.chip)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

@Preview
@Composable
private fun Preview() {
    var mediaType by remember { mutableStateOf(CalendarMediaType.All) }
    val genres = listOf(
        Genre("Action", "action"),
        Genre("Comedy", "comedy"),
        Genre("Drama", "drama"),
        Genre("Science Fiction", "science-fiction"),
        Genre("Talk Show", "talk-show"),
    )
    var genre by remember { mutableStateOf(genres.first()) }
    SnacTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page),
        ) {
            CalendarFilters(
                mediaType = mediaType,
                onMediaTypeChange = { mediaType = it },
                genre = genre,
                genres = genres,
                onGenreChange = { genre = it },
            )
        }
    }
}
