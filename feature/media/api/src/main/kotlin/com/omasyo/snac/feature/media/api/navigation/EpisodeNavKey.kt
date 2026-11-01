package com.omasyo.snac.feature.media.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeNavKey(val showSlug: String, val season: Int, val episode: Int) : NavKey
