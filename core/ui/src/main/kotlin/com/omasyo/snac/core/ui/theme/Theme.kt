package com.omasyo.snac.core.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.colors.ColorScheme
import com.omasyo.snac.core.ui.theme.colors.Colors
import com.omasyo.snac.core.ui.theme.colors.CuttingRoom
import com.omasyo.snac.core.ui.theme.colors.LocalColors

@Composable
fun SnacTheme(
    colors: Colors = CuttingRoom.light,
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalIndication provides SilhouettePressIndication(),
        content = content
    )
}