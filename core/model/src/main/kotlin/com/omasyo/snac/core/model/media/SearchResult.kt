package com.omasyo.snac.core.model.media

sealed interface SearchResult {
    val score: Double?
}

data class MediaResult(
    val media: Media,
    override val score: Double?,
) : SearchResult

data class EpisodeResult(
    val episode: Episode,
    val show: Show,
    override val score: Double?,
) : SearchResult
