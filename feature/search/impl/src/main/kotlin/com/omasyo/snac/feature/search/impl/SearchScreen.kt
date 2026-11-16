package com.omasyo.snac.feature.search.impl

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.common.SnacException
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingSearchItem
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.LocalBottomBarPadding
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.TextField
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchScreen(
    onMediaClick: (Media) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllMoviesClick: () -> Unit,
    onSeeAllShowsClick: () -> Unit,
    onSeeAllPeopleClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = metroViewModel(),
) {
    SearchScreen(
        onMediaClick = onMediaClick,
        onPersonClick = onPersonClick,
        onSeeAllMoviesClick = onSeeAllMoviesClick,
        onSeeAllShowsClick = onSeeAllShowsClick,
        onSeeAllPeopleClick = onSeeAllPeopleClick,
        searchState = viewModel.searchState,
        onSearch = viewModel::search,
        query = viewModel.query.collectAsStateWithLifecycle().value,
        trendingSearches = viewModel.trendingSearches.collectAsLazyPagingItems(),
        movies = viewModel.movies.collectAsLazyPagingItems(),
        shows = viewModel.shows.collectAsLazyPagingItems(),
        people = viewModel.people.collectAsLazyPagingItems(),
        modifier = modifier
    )
}

@Composable
fun SearchScreen(
    onMediaClick: (Media) -> Unit,
    onPersonClick: (Person) -> Unit,
    onSeeAllMoviesClick: () -> Unit,
    onSeeAllShowsClick: () -> Unit,
    onSeeAllPeopleClick: () -> Unit,
    searchState: TextFieldState,
    onSearch: () -> Unit,
    query: String,
    trendingSearches: LazyPagingItems<TrendingSearchItem>,
    movies: LazyPagingItems<Movie>,
    shows: LazyPagingItems<Show>,
    people: LazyPagingItems<Person>,
    modifier: Modifier = Modifier,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    BackHandler(isFocused) { isFocused = false }

    SearchScreenLayout(
        modifier = modifier
            .fillMaxSize()
            .background(SnacColors.page),
        header = {
            SearchHeader(
                searchState = searchState,
                isFocused = isFocused,
                onFocusChange = { isFocused = it },
                onSearch = onSearch,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
            )
        },
    ) { innerPadding ->
        AnimatedContent(isFocused) { isFocused ->
            if (isFocused) {
                SearchResultScreen(
                    onMediaClick = onMediaClick,
                    onPersonClick = onPersonClick,
                    onSeeAllMoviesClick = onSeeAllMoviesClick,
                    onSeeAllShowsClick = onSeeAllShowsClick,
                    onSeeAllPeopleClick = onSeeAllPeopleClick,
                    query = query,
                    movies = movies,
                    shows = shows,
                    people = people,
                    contentPadding = innerPadding,
                )
            } else {
                BentoGrid(
                    contentPadding = innerPadding,
                    items = trendingSearches,
                ) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = { onMediaClick(item.media) }
                            )
                    ) {
                        Image(
                            imageUrl = item.media.imageUrl,
                            modifier = Modifier.matchParentSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHeader(
    searchState: TextFieldState,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val shadowColor = SnacColors.signalShadow

    LaunchedEffect(isFocused) {
        if (!isFocused) {
            focusManager.clearFocus()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .rotate(-1f)
                .clickable(
                    indication = SilhouettePressIndication(
                        color = { shadowColor },
                        offset = DpOffset(6.dp, 6.dp)
                    ),
                    interactionSource = interactionSource,
                    onClick = { focusRequester.requestFocus() },
                )
                .border(4.dp, SnacColors.outline)
                .background(SnacColors.film)
                .weight(1f)
        ) {
            ClapperboardStripes(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Icon(
                    icon = R.drawable.magnifying_glass_bold,
                    contentDescription = null,
                    tint = SnacColors.onFilm,
                )
                TextField(
                    state = searchState,
                    placeholder = "Films, series, people, lists",
                    textStyle = SnacTypography.body.copy(color = SnacColors.onFilm),
                    interactionSource = interactionSource,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    onKeyboardAction = { onSearch() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { if (it.isFocused) onFocusChange(true) },
                )
                if (searchState.text.isNotEmpty()) {
                    val tintColor = SnacColors.film
                    Icon(
                        icon = R.drawable.x_bold,
                        contentDescription = null,
                        tint = SnacColors.onFilm,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = TintIndication(
                                contentOnly = true,
                                focusOpacity = 0.3f,
                                pressOpacity = 0.7f,
                            ) { tintColor },
                            onClick = { searchState.clearText() },
                        )

                    )
                }
            }
        }

        val tintColor = SnacColors.page
        AnimatedVisibility(isFocused) {
            Text(
                text = "Cancel",
                style = SnacTypography.hand,
                color = SnacColors.signal,
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = TintIndication(
                            contentOnly = true,
                            focusOpacity = 0.2f,
                            pressOpacity = 0.5f
                        ) { tintColor },
                        onClick = { onFocusChange(false) },
                    )
                    .padding(12.dp),
            )
        }
    }
}

@Composable
private fun SearchScreenLayout(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {

    SubcomposeLayout(modifier) { constraint ->
        val headerPlaceable =
            subcompose("header") { header() }.first().measure(constraint.copy(minHeight = 0))

        val headerHeight = headerPlaceable.height.toDp()


        val gridPlaceable =
            subcompose("grid") {
                val bottomBarPadding = LocalBottomBarPadding.current
                content(
                    PaddingValues(
                        top = headerHeight,
                        bottom = bottomBarPadding.calculateBottomPadding(),
                    )
                )
            }.first()
                .measure(constraint)

        layout(gridPlaceable.width, gridPlaceable.height) {
            gridPlaceable.placeRelative(0, 0)
            headerPlaceable.placeRelative(0, 0)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val trendingSearches = remember {
        val sample =
            List(3) { Movie.PreviewSample }.flatten().map { TrendingSearchItem(it, count = 1204) }
        flowOf(PagingData.from(sample))
    }.collectAsLazyPagingItems()

    val movies =
        remember { flowOf(PagingData.from(Movie.PreviewSample)) }.collectAsLazyPagingItems()
    val shows = remember { flowOf(PagingData.from(Show.PreviewSample)) }.collectAsLazyPagingItems()
    val people =
        remember { flowOf(PagingData.from(Cast.PreviewSample.map { it.person })) }.collectAsLazyPagingItems()

    SnacTheme {
        SearchScreen(
            onMediaClick = {},
            onPersonClick = {},
            onSeeAllMoviesClick = {},
            onSeeAllShowsClick = {},
            onSeeAllPeopleClick = {},
            searchState = rememberTextFieldState(),
            onSearch = {},
            query = "Help",
            trendingSearches = trendingSearches,
            movies = movies,
            shows = shows,
            people = people,
        )
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    val trendingFlow = remember {
        MutableStateFlow(
            PagingData.empty<TrendingSearchItem>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading,
                )
            )
        )
    }
    val moviesFlow = remember {
        MutableStateFlow(
            PagingData.empty<Movie>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading,
                )
            )
        )
    }
    val showsFlow = remember {
        MutableStateFlow(
            PagingData.empty<Show>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading,
                )
            )
        )
    }
    val peopleFlow = remember {
        MutableStateFlow(
            PagingData.empty<Person>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading,
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        val error = LoadState.Error(SnacException(SnacResult.Error.Unexpected))
        trendingFlow.value = PagingData.empty(LoadStates(error, error, error))
    }

    val trendingSearches = trendingFlow.collectAsLazyPagingItems()
    SnacTheme {
        SearchScreen(
            onMediaClick = {},
            onPersonClick = {},
            onSeeAllMoviesClick = {},
            onSeeAllShowsClick = {},
            onSeeAllPeopleClick = {},
            searchState = rememberTextFieldState(),
            onSearch = {},
            query = "",
            trendingSearches = trendingSearches,
            movies = moviesFlow.collectAsLazyPagingItems(),
            shows = showsFlow.collectAsLazyPagingItems(),
            people = peopleFlow.collectAsLazyPagingItems(),
        )
    }
}