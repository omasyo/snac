package com.omasyo.snac.core.model.settings

enum class AppTheme {
    CuttingRoom,
    TwoStrip,
    Noir,
    Nitrate,
    Synthwave,
    Phosphor,
    Blueprint,
    DriveIn,
    Marigold,
    Gothic,
    PastelHotel,
}

enum class ThemeMode {
    Light,
    Dark,
    Auto,
}

data class ThemeSettings(
    val theme: AppTheme = AppTheme.CuttingRoom,
    val mode: ThemeMode = ThemeMode.Auto,
)
