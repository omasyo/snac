package com.omasyo.snac.feature.settings.impl

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.settings.AppTheme
import com.omasyo.snac.core.model.settings.ThemeMode
import com.omasyo.snac.core.model.settings.ThemeSettings
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.Colors
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.core.ui.theme.colors.colorScheme
import com.omasyo.snac.core.ui.theme.colors.colors
import com.omasyo.snac.core.ui.theme.isDark
import kotlin.random.Random

@Composable
internal fun AppearanceCard(
    settings: ThemeSettings,
    onThemeChange: (AppTheme) -> Unit,
    onModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val darkTheme = settings.mode.isDark()
    val modeLabels = ThemeMode.entries.associateWith { stringResource(it.label) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .silhouette(offset = DpOffset(4.dp, 4.dp))
            .border(2.dp, SnacColors.ink)
            .background(SnacColors.card)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.theme_label),
            style = SnacTypography.overline,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.selectableGroup(),
        ) {
            AppTheme.entries.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { theme ->

                        val rotation = Random(theme.hashCode()).nextInt(-2, 2).toFloat()
                        ThemeOption(
                            name = stringResource(theme.label),
                            colors = theme.colorScheme.colors(darkTheme),
                            selected = theme == settings.theme,
                            onClick = { onThemeChange(theme) },
                            modifier = Modifier
                                .rotate(rotation)
                                .weight(1f),
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
        Text(
            text = stringResource(R.string.mode_label),
            style = SnacTypography.overline,
            modifier = Modifier.padding(top = 4.dp),
        )
        TabRow(
            selectedTab = settings.mode,
            onSelectedTabChange = onModeChange,
            name = { modeLabels.getValue(it) },
            equalWidth = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ThemeOption(
    name: String,
    colors: Colors,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectionColor = SnacColors.signal
    val shadowColor = if (selected) selectionColor else colors.shadow

    Column(
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = SilhouettePressIndication({ shadowColor }),
                role = Role.RadioButton,
                onClick = onClick,
            )
            .border(2.dp, if (selected) selectionColor else colors.ink)
            .background(SnacColors.film)
            .padding(top = 16.dp)
            .background(colors.page)
            .padding(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            listOf(colors.accent, colors.signal, colors.tape, colors.card).forEach {
                val rotation = Random(it.hashCode()).nextInt(-3, 3).toFloat()
                Box(
                    Modifier
                        .rotate(rotation)
                        .size(24.dp)
                        .border(1.5.dp, colors.ink)
                        .background(it)
                )
            }
        }
        Text(
            text = name,
            style = SnacTypography.subtitle,
            color = colors.ink,
            maxLines = 1,
        )
    }
}

@get:StringRes
private val AppTheme.label: Int
    get() = when (this) {
        AppTheme.CuttingRoom -> R.string.theme_cutting_room
        AppTheme.TwoStrip -> R.string.theme_two_strip
        AppTheme.Noir -> R.string.theme_noir
        AppTheme.Nitrate -> R.string.theme_nitrate
        AppTheme.Synthwave -> R.string.theme_synthwave
        AppTheme.Phosphor -> R.string.theme_phosphor
        AppTheme.Blueprint -> R.string.theme_blueprint
        AppTheme.DriveIn -> R.string.theme_drive_in
        AppTheme.Marigold -> R.string.theme_marigold
        AppTheme.Gothic -> R.string.theme_gothic
        AppTheme.PastelHotel -> R.string.theme_pastel_hotel
    }

@get:StringRes
private val ThemeMode.label: Int
    get() = when (this) {
        ThemeMode.Light -> R.string.mode_light
        ThemeMode.Dark -> R.string.mode_dark
        ThemeMode.Auto -> R.string.mode_auto
    }

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            AppearanceCard(
                settings = ThemeSettings(mode = ThemeMode.Light),
                onThemeChange = {},
                onModeChange = {},
            )
        }
    }
}
