package com.omasyo.snac.feature.media.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.impl.movie.MovieScreen
import com.omasyo.snac.feature.media.impl.show.EpisodeScreen
import com.omasyo.snac.feature.media.impl.show.SeasonScreen
import com.omasyo.snac.feature.media.impl.show.ShowScreen

fun EntryProviderScope<NavKey>.mediaEntry(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onSeasonClick: (SeasonNavKey) -> Unit,
    onEpisodeClick: (EpisodeNavKey) -> Unit,
) {
    entry<MovieNavKey> {
        MovieScreen(
            onBackClick = onBackClick,
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = onMediaClick,
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<ShowNavKey> { key ->
        ShowScreen(
            onBackClick = onBackClick,
            onSeasonClick = { onSeasonClick(SeasonNavKey(key.slug, it.number)) },
            onPersonClick = {},
            onSeeAllCastClick = {},
            onMediaClick = onMediaClick,
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<SeasonNavKey> { key ->
        SeasonScreen(
            onBackClick = onBackClick,
            onEpisodeClick = { onEpisodeClick(EpisodeNavKey(key.showSlug, it.season, it.number)) },
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
        )
    }
    entry<EpisodeNavKey> {
        EpisodeScreen(
            onBackClick = onBackClick,
            onPersonClick = {},
            onSeeAllCastClick = {},
            onReviewClick = {},
            onSeeAllReviewClick = {},
            onCommentClick = {},
        )
    }
}
