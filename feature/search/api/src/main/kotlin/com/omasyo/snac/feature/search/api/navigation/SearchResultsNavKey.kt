package com.omasyo.snac.feature.search.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SearchMoviesNavKey(val initialIndex: Int) : NavKey

@Serializable
data class SearchShowsNavKey(val initialIndex: Int) : NavKey

@Serializable
data class SearchPeopleNavKey(val initialIndex: Int) : NavKey
