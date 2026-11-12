package com.omasyo.snac.feature.media.impl.people

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.ui.models.mapOrNull
import com.omasyo.snac.feature.media.impl.show.displayTitle
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun EpisodePeopleScreen(
    showSlug: String,
    season: Int,
    episode: Int,
    onBackClick: () -> Unit,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EpisodePeopleViewModel =
        assistedMetroViewModel<EpisodePeopleViewModel, EpisodePeopleViewModel.Factory> {
            create(showSlug, season, episode)
        },
) {
    val show = viewModel.show.collectAsStateWithLifecycle().value.mapOrNull { it }
    val episodeDetails = viewModel.episode.collectAsStateWithLifecycle().value.mapOrNull { it }

    PeopleScreen(
        hero = episodeDetails?.let {
            HeroContent(
                title = it.displayTitle(),
                posterUrl = show?.posterUrl,
                secondaryPosterUrl = it.screenshotUrl,
                extra = listOfNotNull(show?.title, it.code()).joinToString(" · "),
            )
        } ?: HeroContent.Empty,
        onBackClick = onBackClick,
        onPersonClick = onPersonClick,
        peopleList = viewModel.people.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

private fun EpisodeDetails.code() =
    "S%02dE%02d".format(season, number)
