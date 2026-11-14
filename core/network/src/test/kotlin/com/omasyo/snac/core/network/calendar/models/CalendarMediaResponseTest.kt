package com.omasyo.snac.core.network.calendar.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

private const val JSON = """[
  {
    "movie": {
      "ids": { "imdb": "tt5050788", "plex": { "guid": "5d776c1696b655001fe2586d", "slug": "loev" }, "slug": "loev-2016", "tmdb": 381070, "trakt": 225433 },
      "year": 2016,
      "title": "Loev"
    },
    "released": "2026-10-13"
  },
  {
    "show": {
      "ids": { "imdb": "tt0159847", "plex": { "guid": "5d9c08604eefaa001f5db97b", "slug": "antiques-roadshow-us" }, "slug": "antiques-roadshow-us", "tmdb": 33739, "tvdb": 75117, "trakt": 33593 },
      "year": 1997,
      "title": "Antiques Roadshow (US)",
      "aired_episodes": 681
    },
    "episode": {
      "ids": { "imdb": null, "plex": { "guid": null }, "tmdb": null, "tvdb": 11926388, "trakt": 14480050 },
      "title": "Vintage First Finds 4",
      "number": 4,
      "season": 31
    },
    "first_aired": "2026-10-13T00:00:00.000Z",
    "released": null
  },
  {
    "show": {
      "ids": { "imdb": "tt35051210", "plex": { "guid": "6aad0dd0bbb7cb58338bcc0f", "slug": "tyson-1" }, "slug": "tyson", "tmdb": 335536, "tvdb": 482647, "trakt": 328486 },
      "year": 2026,
      "title": "TYSON",
      "aired_episodes": 4
    },
    "episode": {
      "ids": { "imdb": "tt39062813", "plex": { "guid": "6aad0dd0bbb7cb58338bcc1f" }, "tmdb": 7817528, "tvdb": 11985784, "trakt": 14505189 },
      "title": "Power",
      "number": 1,
      "season": 1,
      "episode_type": "multiple_episodes",
      "episodes": [
        { "ids": { "trakt": 14505189 }, "title": "Power", "number": 1, "season": 1 },
        { "ids": { "trakt": 14505190 }, "title": "Greed", "number": 2, "season": 1 }
      ]
    },
    "first_aired": "2026-10-13T04:00:00.000Z",
    "released": "2026-10-13"
  }
]"""

class CalendarMediaResponseTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun decodesMoviesAndShows() {
        val result = json.decodeFromString<List<CalendarMediaResponse>>(JSON)

        assertEquals(3, result.size)
        assert(result[0] is CalendarMovieResponse)

        val single = result[1] as CalendarShowResponse
        assertNull(single.released)
        assertNull(single.episode.episodes)

        val multi = result[2] as CalendarShowResponse
        assertEquals("multiple_episodes", multi.episode.episodeType)
        assertEquals(2, multi.episode.episodes?.size)
    }
}
