package com.omasyo.snac.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = SnacTypography.subtitle,
    backgroundColor: Color = SnacColors.accent,
    contentColor: Color = SnacColors.onAccent,
    borderColor: Color = SnacColors.ink,
    shadowColor: Color = SnacColors.shadow,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

) {
    Button(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        shadowColor = shadowColor
    ) {
        BasicText(
            text = text,
            style = style,
            color = { contentColor },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnacColors.accent,
    borderColor: Color = SnacColors.ink,
    shadowColor: Color = SnacColors.shadow,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(
                indication = SilhouettePressIndication({ shadowColor }),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .background(backgroundColor)
            .border(2.dp, borderColor)
    ) {
        content()
    }
}

@Composable
fun IconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnacColors.accent,
    contentColor: Color = SnacColors.onAccent,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = backgroundColor,
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier
                .size(24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SnacTheme {
        Button(text = "Sign in", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}