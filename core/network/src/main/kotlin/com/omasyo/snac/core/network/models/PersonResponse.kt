package com.omasyo.snac.core.network.models

import com.omasyo.snac.core.network.serializers.InstantSerializer
import com.omasyo.snac.core.network.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
data class PersonResponse(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("name")
    val name: String,
    @SerialName("images")
    val images: Images?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("biography")
    val biography: String?,
    @SerialName("birthday")
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate?,
    @SerialName("death")
    @Serializable(with = LocalDateSerializer::class)
    val death: LocalDate?,
    @SerialName("birthplace")
    val birthplace: String?,
    @SerialName("known_for_department")
    val knownForDepartment: String?,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("social_ids")
    val socialIds: SocialIdsResponse?,
    @SerialName("updated_at")
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
) {
    @Serializable
    data class Images(
        @SerialName("headshot")
        val headshot: List<String>,
        @SerialName("fanart")
        val fanart: List<String>,
    )
}
