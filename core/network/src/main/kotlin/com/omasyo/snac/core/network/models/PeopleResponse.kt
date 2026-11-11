package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeopleResponse(
    @SerialName("cast")
    val cast: List<CastResponse>,
    @SerialName("crew")
    val crew: Map<String, List<CrewResponse>>,
) {
    @Serializable
    data class CastResponse(
        @SerialName("characters")
        val characters: List<String>,
        @SerialName("person")
        val person: Person,
        @SerialName("images")
        val images: Images?,
    )

    @Serializable
    data class CrewResponse(
        @SerialName("jobs")
        val jobs: List<String>,
        /** Episodes worked on. Shows, seasons and episodes only; absent for movies. */
        @SerialName("episode_count")
        val episodeCount: Int?,
        @SerialName("person")
        val person: Person,
        @SerialName("images")
        val images: Images?,
    )

    @Serializable
    data class Person(
        @SerialName("ids")
        val ids: Ids,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class Images(
        @SerialName("headshot")
        val headshot: List<String>,
    )
}
