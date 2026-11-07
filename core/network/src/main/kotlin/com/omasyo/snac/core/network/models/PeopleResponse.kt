package com.omasyo.snac.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeopleResponse(
    @SerialName("cast")
    val cast: List<CastResponse>?,
) {
    @Serializable
    data class CastResponse(
        @SerialName("character")
        val character: String?,
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
