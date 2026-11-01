package com.omasyo.snac.feature.settings.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.model.settings.AppTheme
import com.omasyo.snac.core.model.settings.ThemeMode
import com.omasyo.snac.core.model.settings.ThemeSettings
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.SignInPrompt
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.CuttingRoom
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import com.omasyo.snac.core.ui.R as UiR

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = metroViewModel(),
) {
    SettingsScreen(
        onBackClick = onBackClick,
        onSignInClick = onSignInClick,
        onSignOutClick = viewModel::signOut,
        profileState = viewModel.profileState.collectAsStateWithLifecycle().value,
        themeSettings = viewModel.themeSettings.collectAsStateWithLifecycle().value,
        onThemeChange = viewModel::setTheme,
        onThemeModeChange = viewModel::setThemeMode,
        modifier = modifier,
    )
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    profileState: ProfileState,
    themeSettings: ThemeSettings,
    onThemeChange: (AppTheme) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(SnacColors.page)
            .bottomBarPadding()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                icon = UiR.drawable.caret_left_bold,
                contentDescription = stringResource(R.string.back),
                onClick = onBackClick,
                backgroundColor = SnacColors.chip,
                contentColor = SnacColors.ink,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = stringResource(R.string.settings_title),
                style = SnacTypography.headline,
                modifier = Modifier
                    .rotate(-2f)
                    .silhouette(SnacColors.signal),
            )
        }
        when (profileState) {
            ProfileState.SignedOut -> SignInPrompt(
                title = stringResource(R.string.sign_in_title),
                message = stringResource(R.string.sign_in_message),
                onSignInClick = onSignInClick,
                modifier = Modifier.fillMaxWidth(),
            )

            else -> AccountSection(
                state = profileState,
                onSignOutClick = onSignOutClick,
            )
        }
        Text(
            text = stringResource(R.string.appearance_title),
            style = SnacTypography.title,
            modifier = Modifier.silhouette(SnacColors.tape),
        )
        AppearanceCard(
            settings = themeSettings,
            onThemeChange = onThemeChange,
            onModeChange = onThemeModeChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AccountSection(
    state: ProfileState,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        Text(
            text = when (state) {
                is ProfileState.Loaded -> with(state.profile) {
                    "@$username\n${name.orEmpty()}\nVIP: $isVip"
                }

                is ProfileState.Failed -> state.message
                else -> stringResource(R.string.profile_loading)
            },
            style = SnacTypography.body,
        )
        Button(
            text = stringResource(R.string.sign_out),
            onClick = onSignOutClick,
        )
    }
}

@Preview(device = "spec:width=1080px,height=5000px,dpi=440")
@Composable
private fun Preview() {
    SnacTheme {
        SettingsScreen(
            onBackClick = {},
            onSignInClick = {},
            onSignOutClick = {},
            profileState = ProfileState.SignedOut,
            themeSettings = ThemeSettings(mode = ThemeMode.Light),
            onThemeChange = {},
            onThemeModeChange = {},
        )
    }
}

@Preview(device = "spec:width=1080px,height=3000px,dpi=440")
@Composable
private fun DarkPreview() {
    SnacTheme(CuttingRoom.dark) {
        SettingsScreen(
            onBackClick = {},
            onSignInClick = {},
            onSignOutClick = {},
            profileState = ProfileState.SignedOut,
            themeSettings = ThemeSettings(mode = ThemeMode.Dark),
            onThemeChange = {},
            onThemeModeChange = {},
        )
    }
}
