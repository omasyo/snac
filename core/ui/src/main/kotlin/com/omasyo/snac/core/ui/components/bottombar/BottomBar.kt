package com.omasyo.snac.core.ui.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors


enum class BottomBarDestination(
    val label: String,
//    val route: NavRoute,
) {
    Home("Home"),
    Charts("Charts"),
    Search("Search"),
    Calendar("Calendar"),
//    Me("Me"),
}

@Composable
fun BottomBar(
    currentDestination: BottomBarDestination,
    onCurrentDestinationChange: (BottomBarDestination) -> Unit,
    destinations: List<BottomBarDestination>,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .rotate(-1.5f)
            .bleedHorizontally(8.dp)
            .fillMaxWidth()
            .background(SnacColors.tape)
            .padding(horizontal = 8.dp)
            .padding(horizontal =  16.dp, vertical = 8.dp),
    ) {
        for (destination in destinations) {
            Box(
                modifier = Modifier.widthIn(64.dp),
                contentAlignment = Alignment.Center,
            ) {
                BottomBarItem(
                    destination = destination,
                    isSelected = currentDestination == destination,
                    onClick = { onCurrentDestinationChange(destination) }
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    destination: BottomBarDestination,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val indicationColor = SnacColors.onFilm

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = mutableInteractionSource,
                indication =  remember { TintIndication(true) { indicationColor } },
                onClick = onClick,
            )
    ) {
        Box(
            modifier = Modifier.padding(4.dp).drawBehind {
                if (isSelected) {
                    drawOval(Color.Red, style = Stroke(width = 2.dp.toPx()))
                }
            }
        ) {
            Text(
                text = destination.label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var destination by remember { mutableStateOf(BottomBarDestination.Home) }

    SnacTheme {
        BottomBar(
            currentDestination = destination,
            onCurrentDestinationChange = { destination = it },
            destinations = BottomBarDestination.entries,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

