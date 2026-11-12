package com.omasyo.snac.feature.media.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.MoviePeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.PeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowPeopleNavKey
import com.omasyo.snac.feature.media.impl.movie.MovieScreen
import com.omasyo.snac.feature.media.impl.people.MoviePeopleScreen
import com.omasyo.snac.feature.media.impl.people.ShowPeopleScreen
import com.omasyo.snac.feature.media.impl.show.EpisodeScreen
import com.omasyo.snac.feature.media.impl.show.SeasonScreen
import com.omasyo.snac.feature.media.impl.show.ShowScreen

fun EntryProviderScope<NavKey>.mediaEntry(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onSeasonClick: (SeasonNavKey) -> Unit,
    onEpisodeClick: (EpisodeNavKey) -> Unit,
    onPeopleClick: (PeopleNavKey) -> Unit,
    onPersonClick: (Person) -> Unit,
) {
    entry<MovieNavKey> { key ->
        MovieScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
            onSeeAllCastClick = { onPeopleClick(MoviePeopleNavKey(key.slug)) },
            onMediaClick = onMediaClick,
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<ShowNavKey> { key ->
        ShowScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onSeasonClick = { onSeasonClick(SeasonNavKey(key.slug, it.number)) },
            onPersonClick = onPersonClick,
            onSeeAllCastClick = { onPeopleClick(ShowPeopleNavKey(key.slug)) },
            onMediaClick = onMediaClick,
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<SeasonNavKey> { key ->
        SeasonScreen(
            showSlug = key.showSlug,
            season = key.season,
            onBackClick = onBackClick,
            onEpisodeClick = { onEpisodeClick(EpisodeNavKey(key.showSlug, it.season, it.number)) },
            onPersonClick = onPersonClick,
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
        )
    }
    entry<EpisodeNavKey> { key ->
        EpisodeScreen(
            showSlug = key.showSlug,
            season = key.season,
            episode = key.episode,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
        )
    }
    entry<MoviePeopleNavKey> { key ->
        MoviePeopleScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
        )
    }
    entry<ShowPeopleNavKey> { key ->
        ShowPeopleScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
        )
    }
}
