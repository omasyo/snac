package com.omasyo.snac.feature.search.impl.components

import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.omasyo.snac.core.ui.navigation.navSharedBounds


@Composable
internal fun Modifier.searchResultSharedBounds(slug: String): Modifier =
    navSharedBounds(key = slug, resizeMode = ResizeMode.scaleToBounds())
