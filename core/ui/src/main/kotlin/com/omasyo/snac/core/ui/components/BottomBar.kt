package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.theme.LocalContentColor
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun <T> BottomBar(
    currentDestination: T,
    onCurrentDestinationChange: (T) -> Unit,
    expanded: Boolean,
    destinations: List<T>,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
//    val progress = remember { Animatable(if (expanded) 1f else 0f)  }
    val progress by animateFloatAsState(if (expanded) 0f else 1f)

//    val handleSize = 40.dp

    BottomBarContent(
        currentDestination = currentDestination,
        onCurrentDestinationChange = onCurrentDestinationChange,
        destinations = destinations,
        label = label,
        modifier = modifier
            .navigationBarsPadding()
            .rotate(-1.5f) // FIXME: should be -1.5f
            .graphicsLayer {
//                val maxOffset = size.width - handleSize.toPx()
                translationX = progress * size.width
            }
    )
}

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
            .bleedHorizontally(24.dp)
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

