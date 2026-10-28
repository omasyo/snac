package com.omasyo.snac.core.network.movie.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    @SerialName("collectors")
    val collectors: Int,
    @SerialName("comments")
    val comments: Int,
    @SerialName("favorited")
    val favorited: Int,
    @SerialName("lists")
    val lists: Int,
    @SerialName("plays")
    val plays: Int,
    @SerialName("recommended")
    val recommended: Int,
    @SerialName("votes")
    val votes: Int,
    @SerialName("watchers")
    val watchers: Int
)