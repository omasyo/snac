package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Crew
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.network.models.PeopleResponse

fun PeopleResponse.toCast(): List<Cast> =
    cast.map { it.toCast() }

fun PeopleResponse.toPeopleList() = PeopleList(
    cast = toCast(),
    crew = crew.mapValues { (_, crew) -> crew.map { it.toCrew() } },
)

private fun PeopleResponse.CastResponse.toCast() = Cast(
    characters = characters,
    person = person.toPerson(),
)


private fun PeopleResponse.CrewResponse.toCrew() = Crew(
    jobs = jobs,
    person = person.toPerson(),
)
