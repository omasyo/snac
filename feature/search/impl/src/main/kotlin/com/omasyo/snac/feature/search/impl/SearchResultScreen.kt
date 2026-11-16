package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.search.impl.components.MediaResultCard
import com.omasyo.snac.feature.search.impl.components.NoResultsPlaceholder
import com.omasyo.snac.feature.search.impl.components.PersonResultCard
import com.omasyo.snac.feature.search.impl.components.SearchPromptPlaceholder
import com.omasyo.snac.feature.search.impl.components.searchResultSharedBounds
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchResultScreen(
    onMediaClick: (Media) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllMoviesClick: (startIndex: Int) -> Unit,
    onSeeAllShowsClick: (startIndex: Int) -> Unit,
    onSeeAllPeopleClick: (startIndex: Int) -> Unit,
    query: String,
    movies: LazyPagingItems<Movie>,
    shows: LazyPagingItems<Show>,
    people: LazyPagingItems<Person>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
) {
    Box(modifier) {
        val hasNoResults = movies.hasNoResults() && shows.hasNoResults() && people.hasNoResults()
        when {
            query.isBlank() -> SearchPromptPlaceholder()

            hasNoResults ->
                NoResultsPlaceholder(query)

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding),
                ) {

                    if (!movies.hasNoResults()) {
                        SearchResultSection(
                            title = "Movie",
                            onSeeAllClick = onSeeAllMoviesClick,
                            items = movies,
                        ) {
                            MediaResultCard(
                                media = it,
                                onClick = { onMediaClick(it) },
                                modifier = Modifier.searchResultSharedBounds(it.slug),
                            )
                        }
                    }
                    if (!shows.hasNoResults()) {
                        SearchResultSection(
                            title = "Show",
                            onSeeAllClick = onSeeAllShowsClick,
                            items = shows,
                        ) {
                            MediaResultCard(
                                media = it,
                                onClick = { onMediaClick(it) },
                                modifier = Modifier.searchResultSharedBounds(it.slug),
                            )
                        }
                    }
                    if (!people.hasNoResults()) {
                        SearchResultSection(
                            title = "People",
                            onSeeAllClick = onSeeAllPeopleClick,
                            items = people,
                        ) {
                            PersonResultCard(
                                person = it,
                                onClick = { onPersonClick(it) },
                                modifier = Modifier.searchResultSharedBounds(it.slug),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyPagingItems<*>.hasNoResults(): Boolean =
    loadState.refresh is LoadState.NotLoading && itemCount == 0

@Preview
@Composable
private fun Preview() {
    val movies =
        remember { flowOf(PagingData.from(Movie.PreviewSample)) }.collectAsLazyPagingItems()
    val shows = remember { flowOf(PagingData.from(Show.PreviewSample)) }.collectAsLazyPagingItems()
    val people =
        remember { flowOf(PagingData.from(Cast.PreviewSample.map { it.person })) }.collectAsLazyPagingItems()

    SnacTheme {
        SearchResultScreen(
            onMediaClick = {},
            onPersonClick = {},
            onSeeAllMoviesClick = {},
            onSeeAllShowsClick = {},
            onSeeAllPeopleClick = {},
            query = "dave",
            movies = movies,
            shows = shows,
            people = people,
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 16.dp),
        )
    }
}