package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun <T> BottomBar(
    currentDestination: T,
    onCurrentDestinationChange: (T) -> Unit,
    destinations: List<T>,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .navigationBarsPadding()
            .rotate(-1.5f)
            .bleedHorizontally(8.dp)
            .fillMaxWidth()
            .background(SnacColors.tape)
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
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

    SnacTheme {
        BottomBar(
            currentDestination = destination,
            onCurrentDestinationChange = { destination = it },
            destinations = TestDestination.entries,
            label = { it.label },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

