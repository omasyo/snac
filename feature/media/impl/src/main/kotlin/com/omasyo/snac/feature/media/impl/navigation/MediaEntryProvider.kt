package com.omasyo.snac.feature.media.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.media.api.navigation.CommentThreadNavKey
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.EpisodePeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieCommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.MoviePeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonPeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowCommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowPeopleNavKey
import com.omasyo.snac.feature.media.impl.comment.CommentThreadScreen
import com.omasyo.snac.feature.media.impl.comment.CommentsScreen
import com.omasyo.snac.feature.media.impl.comment.toThreadNavKey
import com.omasyo.snac.feature.media.impl.movie.MovieScreen
import com.omasyo.snac.feature.media.impl.people.EpisodePeopleScreen
import com.omasyo.snac.feature.media.impl.people.MoviePeopleScreen
import com.omasyo.snac.feature.media.impl.people.SeasonPeopleScreen
import com.omasyo.snac.feature.media.impl.people.ShowPeopleScreen
import com.omasyo.snac.feature.media.impl.show.EpisodeScreen
import com.omasyo.snac.feature.media.impl.show.SeasonScreen
import com.omasyo.snac.feature.media.impl.show.ShowScreen
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey

fun EntryProviderScope<NavKey>.mediaEntry(navigator: Navigator) {
    val onBackClick: () -> Unit = navigator::goBack
    val onMediaClick: (Media) -> Unit = {
        when (it) {
            is Movie -> navigator.navigate(MovieNavKey(it.slug))
            is Show -> navigator.navigate(ShowNavKey(it.slug))
        }
    }
    val onPersonClick: (Person) -> Unit = { navigator.navigate(PersonNavKey(it.slug)) }

    entry<MovieNavKey> { key ->
        MovieScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
            onSeeAllCastClick = { navigator.navigate(MoviePeopleNavKey(key.slug)) },
            onMediaClick = onMediaClick,
            onReviewClick = { navigator.navigate(it.toThreadNavKey()) },
            onSeeAllReviewClick = { navigator.navigate(MovieCommentsNavKey(key.slug)) },
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<ShowNavKey> { key ->
        ShowScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onSeasonClick = { navigator.navigate(SeasonNavKey(key.slug, it.number)) },
            onPersonClick = onPersonClick,
            onSeeAllCastClick = { navigator.navigate(ShowPeopleNavKey(key.slug)) },
            onMediaClick = onMediaClick,
            onReviewClick = { navigator.navigate(it.toThreadNavKey()) },
            onSeeAllReviewClick = { navigator.navigate(ShowCommentsNavKey(key.slug)) },
            onCommentClick = {},
            onSeeAllRelatedClick = {},
        )
    }
    entry<SeasonNavKey> { key ->
        SeasonScreen(
            showSlug = key.showSlug,
            season = key.season,
            onBackClick = onBackClick,
            onEpisodeClick = {
                navigator.navigate(
                    EpisodeNavKey(
                        key.showSlug,
                        it.season,
                        it.number
                    )
                )
            },
            onPersonClick = onPersonClick,
            onSeeAllCastClick = {
                navigator.navigate(
                    SeasonPeopleNavKey(
                        key.showSlug,
                        key.season
                    )
                )
            },
            onReviewClick = { navigator.navigate(it.toThreadNavKey()) },
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
            onSeeAllCastClick = {
                navigator.navigate(EpisodePeopleNavKey(key.showSlug, key.season, key.episode))
            },
            onReviewClick = { navigator.navigate(it.toThreadNavKey()) },
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
    entry<SeasonPeopleNavKey> { key ->
        SeasonPeopleScreen(
            showSlug = key.showSlug,
            season = key.season,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
        )
    }
    entry<EpisodePeopleNavKey> { key ->
        EpisodePeopleScreen(
            showSlug = key.showSlug,
            season = key.season,
            episode = key.episode,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
        )
    }
    entry<MovieCommentsNavKey> { key ->
        CommentsScreen(
            key = key,
            onBackClick = onBackClick,
            onCommentClick = { navigator.navigate(it.toThreadNavKey()) },
        )
    }
    entry<ShowCommentsNavKey> { key ->
        CommentsScreen(
            key = key,
            onBackClick = onBackClick,
            onCommentClick = { navigator.navigate(it.toThreadNavKey()) },
        )
    }
    entry<CommentThreadNavKey> { key ->
        CommentThreadScreen(key = key)
    }
}
