package com.omasyo.snac.feature.media.impl.people

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.mapOrNull
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun MoviePeopleScreen(
    slug: String,
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviePeopleViewModel =
        assistedMetroViewModel<MoviePeopleViewModel, MoviePeopleViewModel.Factory> {
            create(slug)
        },
) {
    val movie = viewModel.movie.collectAsStateWithLifecycle().value
    val people = viewModel.people.collectAsStateWithLifecycle().value
    val directors = people.mapOrNull { it.directors() }.orEmpty()

    PeopleScreen(
        hero = movie.mapOrNull { it.toHeroContent(directors) } ?: HeroContent.Empty,
        onBackClick = onBackClick,
        onPersonClick = onPersonClick,
        peopleList = people,
        modifier = modifier,
    )
}

private fun MovieDetails.toHeroContent(directors: List<String>) = HeroContent(
    title = title,
    posterUrl = posterUrl,
    secondaryPosterUrl = secondaryPosterUrl,
    extra = metaLine(
        year?.toString(),
        directors.takeIf { it.isNotEmpty() }?.joinToString(", "),
    ),
)

private fun PeopleList.directors(): List<String> = crew.entries
    .firstOrNull { it.key.equals("directing", ignoreCase = true) }
    ?.value
    ?.filter { crew -> crew.jobs.any { it.equals("Director", ignoreCase = true) } }
    ?.map { it.person.name }
    .orEmpty()
