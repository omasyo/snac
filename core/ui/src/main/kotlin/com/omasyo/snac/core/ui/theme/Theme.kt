package com.omasyo.snac.core.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.omasyo.snac.core.ui.components.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.colors.CuttingRoom
import com.omasyo.snac.core.ui.theme.colors.LocalColors

@Composable
fun SnacTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColors provides CuttingRoom.light,
        LocalIndication provides SilhouettePressIndication(),
        content = content
    )
}