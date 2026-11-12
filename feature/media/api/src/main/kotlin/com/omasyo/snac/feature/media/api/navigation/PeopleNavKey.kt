package com.omasyo.snac.feature.media.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PeopleNavKey : NavKey {
    val slug: String
}

@Serializable
data class MoviePeopleNavKey(override val slug: String) : PeopleNavKey

@Serializable
data class ShowPeopleNavKey(override val slug: String) : PeopleNavKey
