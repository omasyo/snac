package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.network.models.PeopleResponse

/** De-duplicated by slug: the UI keys the cast row by it. */
fun PeopleResponse.toCast(): List<Cast> = //TODO
    cast.orEmpty()
        .distinctBy { it.person.ids.slug }
        .map { it.toCast() }

private fun PeopleResponse.CastResponse.toCast() = Cast(
    character = character.orEmpty(),
    person = Person(
        name = person.name,
        slug = person.ids.slug,
        imageUrl = images?.headshot?.firstOrNull()?.toImageUrl(),
    ),
)
