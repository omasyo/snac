package com.omasyo.snac.core.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.omasyo.snac.core.model.settings.AppTheme
import com.omasyo.snac.core.model.settings.ThemeMode
import com.omasyo.snac.core.model.settings.ThemeSettings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

@ContributesBinding(AppScope::class)
@Inject
class SettingsRepositoryImpl(
    context: Context,
) : SettingsRepository {
    private val dataStore = context.settingsDataStore

    override val themeSettings: Flow<ThemeSettings> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            val defaults = ThemeSettings()
            ThemeSettings(
                theme = AppTheme.entries.find { it.name == prefs[ThemeKey] } ?: defaults.theme,
                mode = ThemeMode.entries.find { it.name == prefs[ThemeModeKey] } ?: defaults.mode,
            )
        }
        .distinctUntilChanged()

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { it[ThemeKey] = theme.name }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[ThemeModeKey] = mode.name }
    }

    private companion object {
        val ThemeKey = stringPreferencesKey("theme")
        val ThemeModeKey = stringPreferencesKey("theme_mode")
    }
}
