package com.omasyo.snac.feature.media.impl.people

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.mapOrNull
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun ShowPeopleScreen(
    slug: String,
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowPeopleViewModel =
        assistedMetroViewModel<ShowPeopleViewModel, ShowPeopleViewModel.Factory> {
            create(slug)
        },
) {
    val show = viewModel.show.collectAsStateWithLifecycle().value

    PeopleScreen(
        hero = show.mapOrNull { it.toHeroContent() } ?: HeroContent.Empty,
        onBackClick = onBackClick,
        onPersonClick = onPersonClick,
        peopleList = viewModel.people.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

private fun ShowDetails.toHeroContent() = HeroContent(
    title = title,
    posterUrl = posterUrl,
    secondaryPosterUrl = secondaryPosterUrl,
    extra = metaLine(year?.toString(), network),
)
