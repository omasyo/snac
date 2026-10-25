package com.omasyo.snac.core.network.movie.models


import com.omasyo.snac.core.network.models.Movie
import com.omasyo.snac.core.network.models.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class BoxOfficeResponse(
    @SerialName("revenue")
     val revenue: Long,
    @SerialName("movie")
    val movie: Movie,
)