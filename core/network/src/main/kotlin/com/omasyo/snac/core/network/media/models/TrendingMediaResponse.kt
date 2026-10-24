package com.omasyo.snac.core.network.media.models


import com.omasyo.snac.core.network.models.Movie
import com.omasyo.snac.core.network.models.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = MediaSerializer::class)
sealed interface TrendingItem {
    val watchers: Int
}

@Serializable
data class MovieItem(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("movie")
    val movie: Movie,
) : TrendingItem

@Serializable
data class ShowItem(
    @SerialName("watchers")
    override val watchers: Int,
    @SerialName("show")
    val show: Show,
) : TrendingItem


internal object MediaSerializer : JsonContentPolymorphicSerializer<TrendingItem>(
    TrendingItem::class
) {

    override fun selectDeserializer(element: JsonElement) = when {
        "movie" in element.jsonObject -> MovieItem.serializer()
        "show" in element.jsonObject -> ShowItem.serializer()
        else -> throw IllegalArgumentException("Unknown type")
    }
}
