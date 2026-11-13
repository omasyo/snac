package com.omasyo.snac.core.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.bleedHorizontally(bleed: Dp) = layout { measurable, constraints ->
    val extra = bleed.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.maxWidth + extra * 2,
            maxWidth = constraints.maxWidth + extra * 2,
        )
    )
    layout(constraints.maxWidth, placeable.height) {
        placeable.place(-extra, 0)
    }
}

fun Modifier.bleedVertically(bleed: Dp) = layout { measurable, constraints ->
    val extra = bleed.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minHeight = constraints.maxHeight + extra * 2,
            maxHeight = constraints.maxHeight + extra * 2,
        )
    )
    layout(placeable.width, constraints.maxHeight) {
        placeable.place(0, -extra)
    }
}