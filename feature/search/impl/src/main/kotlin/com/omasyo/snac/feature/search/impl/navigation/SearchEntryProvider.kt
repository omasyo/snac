package com.omasyo.snac.feature.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchNavKey
import com.omasyo.snac.feature.search.impl.SearchScreen

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchNavKey> {
        SearchScreen(
            onMediaClick = {
                when (it) {
                    is Movie -> navigator.navigate(MovieNavKey(it.slug))
                    is Show -> navigator.navigate(ShowNavKey(it.slug))
                }
            },
            onPersonClick = { navigator.navigate(PersonNavKey(it.slug)) },
            onSeeAllMoviesClick = {  },
            onSeeAllShowsClick = {  },
            onSeeAllPeopleClick = {},
        )
    }
}
