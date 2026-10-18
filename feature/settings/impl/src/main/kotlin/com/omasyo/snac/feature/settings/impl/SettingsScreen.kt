package com.omasyo.snac.feature.settings.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = metroViewModel(),
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button("Sign out", viewModel::signOut)
        BasicText(
            when (val profile = profileState) {
                ProfileState.Idle -> ""
                ProfileState.Loading -> "Loading profile…"
                is ProfileState.Loaded -> with(profile.profile) {
                    "@$username\n${name.orEmpty()}\nVIP: $isVip"
                }

                is ProfileState.Failed -> "Profile error: ${profile.message}"
            }
        )
    }
}


@Composable
private fun Button(text: String, onClick: () -> Unit) {
    Box(Modifier.clickable(onClick = onClick).padding(16.dp)) {
        BasicText(text)
    }
}
