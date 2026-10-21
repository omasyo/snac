package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.LocalColors


val LocalContentColor =
    compositionLocalWithComputedDefaultOf { LocalColors.currentValue.ink } //TODO move out of here

val LocalTextStyle = compositionLocalOf(structuralEqualityPolicy()) { SnacTypography.body }

// TODO(b/156598010): remove this and replace with fold definition on the backing CompositionLocal
@Composable
fun ProvideTextStyle(value: TextStyle, content: @Composable () -> Unit) {
    val mergedStyle = LocalTextStyle.current.merge(value)
    CompositionLocalProvider(LocalTextStyle provides mergedStyle, content = content)
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) {
    val color = color.takeOrElse { LocalContentColor.current }
    BasicText(
        text = text,
        style = LocalTextStyle.current.merge(style),
        color = { color },
        maxLines = maxLines,
        modifier = modifier,
    )
}