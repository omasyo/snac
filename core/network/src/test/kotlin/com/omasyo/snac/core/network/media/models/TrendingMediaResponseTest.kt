package com.omasyo.snac.core.network.media.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow


const val JSON = """[
  {
    "watchers": 9796,
    "show": {
      "ids": {
        "imdb": "tt31510819",
        "plex": {
          "guid": "65e04d051f43644ac5a79a99",
          "slug": "mobland"
        },
        "slug": "mobland",
        "tmdb": 247718,
        "tvdb": 446831,
        "trakt": 228595
      },
      "year": 2025,
      "title": "MobLand",
      "aired_episodes": 16
    }
  },
  {
    "watchers": 1610,
    "movie": {
      "ids": {
        "imdb": "tt22084616",
        "plex": {
          "guid": "5ed8a6246e2d7200411126cf",
          "slug": "spider-man-brand-new-day"
        },
        "slug": "spider-man-brand-new-day-2026",
        "tmdb": 969681,
        "trakt": 905132
      },
      "year": 2026,
      "title": "Spider-Man: Brand New Day"
    }
  }
]"""

class TrendingMediaResponseTest {
    @Test
    fun test() {
        val jsonConfig = Json { ignoreUnknownKeys = true }


        assert(true)

        assertDoesNotThrow { jsonConfig.decodeFromString<List<TrendingItem>>(JSON) }
    }
}