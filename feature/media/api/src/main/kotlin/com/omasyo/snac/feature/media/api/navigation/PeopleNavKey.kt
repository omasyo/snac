package com.omasyo.snac.feature.media.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PeopleNavKey : NavKey

@Serializable
data class MoviePeopleNavKey(val slug: String) : PeopleNavKey

@Serializable
data class ShowPeopleNavKey(val slug: String) : PeopleNavKey

@Serializable
data class SeasonPeopleNavKey(val showSlug: String, val season: Int) : PeopleNavKey

@Serializable
data class EpisodePeopleNavKey(
    val showSlug: String,
    val season: Int,
    val episode: Int,
) : PeopleNavKey
