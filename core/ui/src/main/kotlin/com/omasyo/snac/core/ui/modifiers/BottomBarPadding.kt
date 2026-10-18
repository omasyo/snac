package com.omasyo.snac.core.ui.modifiers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

val LocalBottomBarPadding = staticCompositionLocalOf { PaddingValues.Zero }


@Composable
fun Modifier.bottomBarPadding() = padding(LocalBottomBarPadding.current)