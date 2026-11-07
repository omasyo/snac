package com.omasyo.snac.core.data.mappers

import android.util.Log
import com.omasyo.snac.core.data.models.Genre
import java.time.LocalDate
import java.time.format.DateTimeParseException

const val TAG = "CommonMappers"

/** Trakt image paths come without a scheme, e.g. `media.trakt.tv/movies/1/medium/poster.webp`. */
internal fun String.toImageUrl(): String = "https://$this"

internal fun String.toGenre(): Genre = Genre(
    name = split('-').joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } } //TODO
)

internal fun String.toLocalDateOrNull(): LocalDate? =
    try {
        LocalDate.parse(this)
    } catch (e: DateTimeParseException) {
        Log.e(TAG, "toLocalDateOrNull: $e")
        null
    }
