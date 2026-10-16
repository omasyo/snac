package com.omasyo.snac.core.ui.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class Colors(
    val page: Color,
    val card: Color,
    val chip: Color,
    val ink: Color,
    val film: Color,
    val onFilm: Color,
    val accent: Color,
    val onAccent: Color,
    val signal: Color,
    val tape: Color,
    val muted: Color,
    val mutedOnFilm: Color,
    val faint: Color,
    val filmLine: Color,
    val rule: Color,
    val shadow: Color,
)

@Immutable
data class ColorScheme(
    val light: Colors,
    val dark: Colors = light,
)


val SnacColors @Composable get() = LocalColors.current

internal val LocalColors = staticCompositionLocalOf { CuttingRoom.light }