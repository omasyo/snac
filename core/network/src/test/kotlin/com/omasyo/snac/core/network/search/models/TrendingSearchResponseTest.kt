package com.omasyo.snac.core.network.search.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val JSON = """[
  {
    "type": "show",
    "id": 303715,
    "count": 2746,
    "show": {
      "ids": {
        "imdb": "tt39943756",
        "plex": { "guid": "68eb30c44333ce69ffaeb3df", "slug": null },
        "slug": "campus-drivers",
        "tmdb": 303044,
        "tvdb": 468646,
        "trakt": 303715
      },
      "year": 2026,
      "title": "Campus Drivers",
      "runtime": 45,
      "certification": "TV-MA",
      "aired_episodes": 6
    }
  },
  {
    "type": "movie",
    "id": 1164118,
    "count": 2199,
    "movie": {
      "ids": {
        "imdb": "tt35538033",
        "plex": { "guid": "67a23f4861078f9079534716", "slug": "resident-evil-2026" },
        "slug": "resident-evil-2026",
        "tmdb": 1423191,
        "trakt": 1164118
      },
      "year": 2026,
      "title": "Resident Evil",
      "released": "2026-09-18"
    }
  }
]"""

class TrendingSearchResponseTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `decodes shows and movies`() {
        val result = json.decodeFromString<List<TrendingSearchResponse>>(JSON)

        val show = result[0] as TrendingSearchShowResponse
        assertEquals(2746, show.count)
        assertEquals("campus-drivers", show.show.ids.slug)

        val movie = result[1] as TrendingSearchMovieResponse
        assertEquals(2199, movie.count)
        assertEquals("Resident Evil", movie.movie.title)
    }
}
