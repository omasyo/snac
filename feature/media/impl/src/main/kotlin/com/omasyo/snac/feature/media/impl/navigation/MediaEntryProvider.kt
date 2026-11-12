package com.omasyo.snac.feature.media.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.feature.media.api.navigation.CommentThreadNavKey
import com.omasyo.snac.feature.media.api.navigation.CommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.EpisodePeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieCommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.MoviePeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.PeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonPeopleNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowCommentsNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowPeopleNavKey
import com.omasyo.snac.feature.media.impl.comment.CommentThreadScreen
import com.omasyo.snac.feature.media.impl.comment.CommentThreadTransitionMetadata
import com.omasyo.snac.feature.media.impl.comment.CommentsScreen
import com.omasyo.snac.feature.media.impl.comment.HeroContent
import com.omasyo.snac.feature.media.impl.movie.MovieScreen
import com.omasyo.snac.feature.media.impl.people.EpisodePeopleScreen
import com.omasyo.snac.feature.media.impl.people.MoviePeopleScreen
import com.omasyo.snac.feature.media.impl.people.SeasonPeopleScreen
import com.omasyo.snac.feature.media.impl.people.ShowPeopleScreen
import com.omasyo.snac.feature.media.impl.show.EpisodeScreen
import com.omasyo.snac.feature.media.impl.show.SeasonScreen
import com.omasyo.snac.feature.media.impl.show.ShowScreen

fun EntryProviderScope<NavKey>.mediaEntry(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeasonClick: (SeasonNavKey) -> Unit,
    onEpisodeClick: (EpisodeNavKey) -> Unit,
    onPeopleClick: (PeopleNavKey) -> Unit,
    onCommentsClick: (CommentsNavKey) -> Unit,
    onCommentThreadClick: (CommentThreadNavKey) -> Unit,
) {
    entry<MovieNavKey> { key ->
        MovieScreen(
            slug = key.slug,
            onBackClick = onBackClick,
            onPersonClick = onPersonClick,
            onSeeAllCastClick = { onPeopleClick(MoviePeopleNavKey(key.slug)) },
            onMediaClick = onMediaClick,
            onReviewClick = {},
            onSeeAllReviewClick = { onCommentsClick(MovieCommentsNavKey(key.slug)) },
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
            onSeeAllReviewClick = { onCommentsClick(ShowCommentsNavKey(key.slug)) },
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
            onSeeAllCastClick = { onPeopleClick(SeasonPeopleNavKey(key.showSlug, key.season)) },
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
            onSeeAllCastClick = {
                onPeopleClick(EpisodePeopleNavKey(key.showSlug, key.season, key.episode))
            },
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
    // TODO replace preview data with real comments
    entry<MovieCommentsNavKey> {
        CommentsScreen(
            hero = HeroContent.Empty,
            comments = Comment.PreviewSample,
            replies = Reply.PreviewSample,
            onBackClick = onBackClick,
            onCommentClick = { onCommentThreadClick(CommentThreadNavKey(it.id)) },
        )
    }
    entry<ShowCommentsNavKey> {
        CommentsScreen(
            hero = HeroContent.Empty,
            comments = Comment.PreviewSample,
            replies = Reply.PreviewSample,
            onBackClick = onBackClick,
            onCommentClick = { onCommentThreadClick(CommentThreadNavKey(it.id)) },
        )
    }
    entry<CommentThreadNavKey>(metadata = CommentThreadTransitionMetadata) { key ->
        CommentThreadScreen(
            comment = Comment.PreviewSample.first { it.id == key.commentId },
            replies = Reply.PreviewSample,
            onBackClick = onBackClick,
        )
    }
}
