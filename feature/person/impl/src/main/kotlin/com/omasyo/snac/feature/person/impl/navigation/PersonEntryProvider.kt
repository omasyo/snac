package com.omasyo.snac.feature.person.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey
import com.omasyo.snac.feature.person.impl.PersonScreen

fun EntryProviderScope<NavKey>.personEntry(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
) {
    entry<PersonNavKey> {
        PersonScreen(
            onBackClick = onBackClick,
            onMediaClick = onMediaClick,
        )
    }
}
