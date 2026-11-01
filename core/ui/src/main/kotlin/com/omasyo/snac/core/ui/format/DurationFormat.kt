package com.omasyo.snac.core.ui.format

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.omasyo.snac.core.ui.R
import kotlin.time.Duration

@Composable
fun Duration.formatRuntime(): String = toComponents { hours, minutes, _, _ ->
    if (hours > 0) stringResource(R.string.runtime_hours_minutes, hours, minutes)
    else stringResource(R.string.runtime_minutes, minutes)
}
