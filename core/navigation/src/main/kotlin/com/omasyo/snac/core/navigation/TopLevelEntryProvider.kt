package com.omasyo.snac.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

@PublishedApi
internal const val TOP_LEVEL_NAV_KEY_METADATA = "com.omasyo.snac.core.navigation.TopLevelNavKey"

inline fun <reified K : TopLevelNavKey> EntryProviderScope<NavKey>.topLevelEntry(
    noinline content: @Composable (K) -> Unit,
) {
    entry<K>(
        metadata = { mapOf(TOP_LEVEL_NAV_KEY_METADATA to it) },
        content = content,
    )
}

val NavEntry<*>.topLevelNavKey: TopLevelNavKey?
    get() = metadata[TOP_LEVEL_NAV_KEY_METADATA] as? TopLevelNavKey
