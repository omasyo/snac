package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.network.models.PeopleResponse

fun PeopleResponse.toCast(): List<Cast> = cast.map { it.toCast() }

private fun PeopleResponse.CastResponse.toCast() = Cast(
    characters = characters,
    person = Person(
        name = person.name,
        slug = person.ids.slug,
        imageUrl = images?.headshot?.firstOrNull()?.toImageUrl(),
    ),
)
