package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchResultScreen(
    onMediaClick: (Media) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllMoviesClick: () -> Unit,
    onSeeAllShowsClick: () -> Unit,
    onSeeAllPeopleClick: () -> Unit,
    movies: LazyPagingItems<Movie>,
    shows: LazyPagingItems<Show>,
    people: LazyPagingItems<Person>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SearchResultSection(
            title = "Movie",
            onSeeAllClick = onSeeAllMoviesClick,
            items = movies,
        ) {
            MediaResultCard(
                media = it,
                onClick = { onMediaClick(it) },
            )
        }
        SearchResultSection(
            title = "Show",
            onSeeAllClick = onSeeAllShowsClick,
            items = shows,
        ) {
            MediaResultCard(
                media = it,
                onClick = { onMediaClick(it) },
            )
        }
        SearchResultSection(
            title = "People",
            onSeeAllClick = onSeeAllPeopleClick,
            items = people,
        ) {
            PersonResultCard(
                person = it,
                onClick = { onPersonClick(it) },
            )
        }
    }
}

@Preview(device = "spec:width=1080px,height=3000px,dpi=440")
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
            movies = movies,
            shows = shows,
            people = people,
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 16.dp),
        )
    }
}