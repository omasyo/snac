package com.omasyo.snac.feature.search.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SearchResultsNavKey : NavKey

@Serializable
data object SearchMoviesNavKey : SearchResultsNavKey

@Serializable
data object SearchShowsNavKey : SearchResultsNavKey

@Serializable
data object SearchPeopleNavKey : SearchResultsNavKey
