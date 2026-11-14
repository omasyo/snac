package com.omasyo.snac.feature.calendar.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.calendar.api.navigation.CalendarNavKey
import com.omasyo.snac.feature.calendar.impl.CalendarScreen
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey

fun EntryProviderScope<NavKey>.calendarEntry(navigator: Navigator) {
    entry<CalendarNavKey> {
        CalendarScreen(
            onMovieClick = { navigator.navigate(MovieNavKey(it.slug)) },
            onEpisodeClick = {
                navigator.navigate(EpisodeNavKey(it.media.slug, it.episode.season, it.episode.number))
            },
        )
    }
}
