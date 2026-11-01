package com.omasyo.snac.core.data.settings

import com.omasyo.snac.core.model.settings.AppTheme
import com.omasyo.snac.core.model.settings.ThemeMode
import com.omasyo.snac.core.model.settings.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeSettings: Flow<ThemeSettings>

    suspend fun setTheme(theme: AppTheme)

    suspend fun setThemeMode(mode: ThemeMode)
}
