package com.omasyo.snac.feature.media.impl.people

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.mapOrNull
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun SeasonPeopleScreen(
    showSlug: String,
    season: Int,
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SeasonPeopleViewModel =
        assistedMetroViewModel<SeasonPeopleViewModel, SeasonPeopleViewModel.Factory> {
            create(showSlug, season)
        },
) {
    val show = viewModel.show.collectAsStateWithLifecycle().value.mapOrNull { it }
    val seasonDetails = viewModel.season.collectAsStateWithLifecycle().value.mapOrNull { it }

    PeopleScreen(
        hero = seasonDetails?.toHeroContent(show) ?: HeroContent.Empty,
        onBackClick = onBackClick,
        onPersonClick = onPersonClick,
        peopleList = viewModel.people.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

private fun Season.toHeroContent(show: ShowDetails?) = HeroContent(
    title = title,
    posterUrl = posterUrl ?: show?.posterUrl,
    secondaryPosterUrl = show?.secondaryPosterUrl,
    extra = metaLine(show?.title, firstAired?.year?.toString()),
)
