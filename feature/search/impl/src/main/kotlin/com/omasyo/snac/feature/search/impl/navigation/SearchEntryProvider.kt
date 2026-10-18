package com.omasyo.snac.feature.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.search.api.navigation.SearchNavKey
import com.omasyo.snac.feature.search.impl.SearchScreen

fun EntryProviderScope<NavKey>.searchEntry() {
    entry<SearchNavKey> {
        SearchScreen()
    }
}
