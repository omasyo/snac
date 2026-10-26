package com.omasyo.snac.core.network.show.models


import com.omasyo.snac.core.network.models.ShowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostFavoritedShowResponse(
    @SerialName("user_count")
     val userCount: Int,
    @SerialName("show")
    val show: ShowResponse,
)