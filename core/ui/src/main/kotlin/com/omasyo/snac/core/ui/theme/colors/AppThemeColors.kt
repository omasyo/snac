package com.omasyo.snac.core.ui.theme.colors

import com.omasyo.snac.core.model.settings.AppTheme

val AppTheme.colorScheme: ColorScheme
    get() = when (this) {
        AppTheme.CuttingRoom -> CuttingRoom
        AppTheme.TwoStrip -> TwoStrip
        AppTheme.Noir -> Noir
        AppTheme.Nitrate -> Nitrate
        AppTheme.Synthwave -> Synthwave
        AppTheme.Phosphor -> Phosphor
        AppTheme.Blueprint -> Blueprint
        AppTheme.DriveIn -> DriveIn
        AppTheme.Marigold -> Marigold
        AppTheme.Gothic -> Gothic
        AppTheme.PastelHotel -> PastelHotel
    }

fun ColorScheme.colors(darkTheme: Boolean): Colors = if (darkTheme) dark else light
