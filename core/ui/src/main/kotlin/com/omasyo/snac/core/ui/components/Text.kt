package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import com.omasyo.snac.core.ui.theme.colors.LocalColors


val LocalContentColor =
    compositionLocalWithComputedDefaultOf { LocalColors.currentValue.ink }

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified
) {
    val color = color.takeOrElse { LocalContentColor.current }
    BasicText(
        text = text,
        style = style,
        color = { color },
        modifier = modifier,
    )
}