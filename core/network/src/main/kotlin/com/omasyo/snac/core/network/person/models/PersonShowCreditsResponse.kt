package com.omasyo.snac.core.network.person.models

import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonShowCreditsResponse(
    @SerialName("cast")
    val cast: List<Cast>?,
    @SerialName("crew")
    val crew: Map<String, List<Crew>>?,
) {
    @Serializable
    data class Cast(
        @SerialName("character")
        val character: String?,
        @SerialName("episode_count")
        val episodeCount: Int?,
        @SerialName("series_regular")
        val seriesRegular: Boolean?,
        @SerialName("show")
        val show: ShowResponse,
    )

    @Serializable
    data class Crew(
        @SerialName("job")
        val job: String?,
        @SerialName("show")
        val show: ShowResponse,
    )
}
