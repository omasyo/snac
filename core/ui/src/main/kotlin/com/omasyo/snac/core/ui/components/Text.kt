package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.omasyo.snac.core.ui.theme.LocalContentColor
import com.omasyo.snac.core.ui.theme.SnacTypography


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
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    autoSize: TextAutoSize?  = null,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    val color = color.takeOrElse { LocalContentColor.current }
    BasicText(
        text = text,
        style = LocalTextStyle.current.merge(style),
        color = { color },
        maxLines = maxLines,
        overflow = overflow,
        autoSize = autoSize,
        onTextLayout = onTextLayout,
        modifier = modifier,
    )
}