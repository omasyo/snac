package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.CreditRole
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.network.person.models.PersonDetailsResponse
import com.omasyo.snac.core.network.person.models.PersonMovieCreditsResponse
import com.omasyo.snac.core.network.person.models.PersonShowCreditsResponse

fun PersonDetailsResponse.toPersonDetails() = PersonDetails(
    slug = ids.slug,
    name = name,
    imageUrl = images?.headshot?.firstOrNull()?.toImageUrl(),
    backdropUrl = images?.fanart?.firstOrNull()?.toImageUrl(),
    summary = summary?.takeIf { it.isNotBlank() },
    biography = biography?.takeIf { it.isNotBlank() },
    knownFor = knownForDepartment?.takeIf { it.isNotBlank() },
    birthday = birthday,
    death = death,
    birthplace = birthplace?.takeIf { it.isNotBlank() },
)

fun PersonMovieCreditsResponse.toPersonCredits(): List<PersonCredit> =
    cast.orEmpty().map {
        PersonCredit(
            media = it.movie.toMovie(),
            role = CreditRole.Acting,
            description = it.character.orEmpty(),
        )
    } + crew.orEmpty().values.flatten().map {
        PersonCredit(
            media = it.movie.toMovie(),
            role = CreditRole.Crew,
            description = it.job.orEmpty(),
        )
    }

fun PersonShowCreditsResponse.toPersonCredits(): List<PersonCredit> =
    cast.orEmpty().map {
        PersonCredit(
            media = it.show.toShow(),
            role = CreditRole.Acting,
            description = it.character.orEmpty(),
            episodeCount = it.episodeCount,
            isRegular = it.seriesRegular == true,
        )
    } + crew.orEmpty().values.flatten().map {
        PersonCredit(
            media = it.show.toShow(),
            role = CreditRole.Crew,
            description = it.job.orEmpty(),
        )
    }
