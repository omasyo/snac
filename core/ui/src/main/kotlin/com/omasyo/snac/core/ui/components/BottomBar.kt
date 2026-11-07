package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.theme.LocalContentColor
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.launch

@Composable
fun <T> BottomBar(
    currentDestination: T,
    onCurrentDestinationChange: (T) -> Unit,
    expanded: Boolean,
    destinations: List<T>,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val state = remember {
        AnchoredDraggableState(if (expanded) BarValue.Expanded else BarValue.Hidden)
    }
    val scope = rememberCoroutineScope()
    val hiddenInsetPx = with(LocalDensity.current) {
        (TapeBleed + TouchExtension * 2 + PeekWidth).toPx() // This thing is the remnant of some math, not a tangible value
    }

    LaunchedEffect(expanded) {
        state.animateTo(if (expanded) BarValue.Expanded else BarValue.Hidden)
    }

    BottomBarContent(
        currentDestination = currentDestination,
        onCurrentDestinationChange = onCurrentDestinationChange,
        destinations = destinations,
        label = label,
        modifier = modifier
            .navigationBarsPadding()
            .rotate(-1.5f)
            .bleedHorizontally(TapeBleed + TouchExtension)
            .onSizeChanged { size ->
                val hiddenOffset = size.width - hiddenInsetPx
                state.updateAnchors(
                    DraggableAnchors {
                        BarValue.Expanded at 0f
                        BarValue.Hidden at hiddenOffset
                    }
                )
            }
            .graphicsLayer {
                translationX = state.offset.takeIf { !it.isNaN() } ?: 0f
            }
            .anchoredDraggable(state, Orientation.Horizontal)
            .clickable(
                enabled = state.settledValue == BarValue.Hidden,
                interactionSource = null,
                indication = null,
            ) {
                scope.launch { state.animateTo(BarValue.Expanded) }
            }
            .padding(start = TouchExtension)
    )
}

private enum class BarValue { Expanded, Hidden }

private val TapeBleed = 24.dp
private val PeekWidth = 24.dp
private val TouchExtension = 32.dp

@Composable
private fun <T> BottomBarContent(
    currentDestination: T,
    onCurrentDestinationChange: (T) -> Unit,
    destinations: List<T>,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(SnacColors.tape)
            .padding(horizontal = 40.dp, vertical = 8.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides SnacColors.onTape) {
            for (destination in destinations) {
                Box(
                    modifier = Modifier.widthIn(64.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SelectableText(
                        label = label(destination),
                        isSelected = currentDestination == destination,
                        onClick = { onCurrentDestinationChange(destination) },
                    )
                }
            }
        }
    }
}

val LocalBottomBarPadding = staticCompositionLocalOf { PaddingValues.Zero }


@Composable
fun Modifier.bottomBarPadding() = padding(LocalBottomBarPadding.current)

private enum class TestDestination(val label: String) {
    Home("Home"),
    Charts("Charts"),
    Search("Search"),
    Calendar("Calendar"),
//    Me("Me"),
}

@Preview
@Composable
private fun Preview() {
    var destination by remember { mutableStateOf(TestDestination.Home) }
    var expanded by remember { mutableStateOf(true) }

    SnacTheme {
        Column {
            Button(onClick = { expanded = !expanded }, modifier = Modifier.size(40.dp)) { }
            BottomBar(
                currentDestination = destination,
                onCurrentDestinationChange = { destination = it },
                expanded = expanded,
                destinations = TestDestination.entries,
                label = { it.label },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

