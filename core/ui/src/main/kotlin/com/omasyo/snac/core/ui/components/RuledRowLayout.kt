package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun RuledRowLayout(
    interactionSource: InteractionSource?,
    verticalAlignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val ruleColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    val indication = remember { TintIndication { indicationColor } }
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(ruleColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .then(
                if (interactionSource == null) Modifier
                else Modifier.indication(interactionSource, indication)
            )
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        RuledRowLayout(
            interactionSource = null,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(SnacColors.film),
        ) {
            Box(
                Modifier
                    .width(56.dp)
                    .aspectRatio(PosterAspectRatio)
                    .background(imageGradient())
            )
            Box(
                Modifier
                    .width(160.dp)
                    .height(18.dp)
                    .background(SnacColors.placeholder)
            )
        }
    }
}
