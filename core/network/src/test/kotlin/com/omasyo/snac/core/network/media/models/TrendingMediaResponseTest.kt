package com.omasyo.snac.core.network.media.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow


const val JSON = """[
  {
    "watchers": 3172,
    "movie": {
      "ids": {
        "imdb": "tt7526136",
        "plex": {
          "guid": "5f40d307bf3e560040d6b66e",
          "slug": "heart-of-the-beast-2026"
        },
        "slug": "heart-of-the-beast-2026",
        "tmdb": 1263337,
        "trakt": 1025172
      },
      "year": 2026,
      "title": "Heart of the Beast",
      "votes": 6087,
      "colors": {
        "poster": [
          "#A9916A",
          "#33392E"
        ]
      },
      "genres": [
        "adventure",
        "thriller"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/movies/1025172/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/movies/1025172/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/movies/1025172/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/movies/1025172/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/movies/1025172/medium/poster.webp"
        ],
        "clearart": [
          "media.trakt.tv/movies/1025172/medium/clearart.webp"
        ]
      },
      "rating": 7.851985931396484,
      "status": "released",
      "country": "us",
      "runtime": 101,
      "tagline": "Survival depends on their bond.",
      "trailer": "https://youtube.com/watch?v=UkMuGKqAux4",
      "homepage": "https://www.heartofthebeastmovie.com",
      "language": "en",
      "overview": "After a harrowing plane crash, Special Forces officer James Belmont and his combat dog, Odin, find themselves stranded deep in the Alaskan wilderness. Together, they are forced into a brutal fight for survival against the elements.",
      "released": "2026-09-25",
      "languages": [
        "en"
      ],
      "subgenres": [
        "military",
        "survival"
      ],
      "social_ids": {
        "twitter": "heartofthebeast",
        "facebook": "heartofthebeast",
        "instagram": "heartofthebeast",
        "wikipedia": "Heart_of_the_Beast"
      },
      "updated_at": "2026-11-12T15:25:43.000Z",
      "after_credits": false,
      "certification": "PG-13",
      "comment_count": 115,
      "during_credits": false,
      "original_title": "Heart of the Beast",
      "available_translations": [
        "ar",
        "az",
        "bg",
        "cs",
        "de",
        "el",
        "en",
        "es",
        "et",
        "fa",
        "fr",
        "he",
        "hr",
        "hu",
        "it",
        "ja",
        "ka",
        "ko",
        "lt",
        "lv",
        "nl",
        "pl",
        "pt",
        "ro",
        "ru",
        "sk",
        "sl",
        "sr",
        "sv",
        "th",
        "tr",
        "uk",
        "ur",
        "vi",
        "zh"
      ]
    }
  },
  {
    "watchers": 8189,
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
      "airs": {
        "day": "Sunday",
        "time": "03:00",
        "timezone": "America/New_York"
      },
      "year": 2025,
      "title": "MobLand",
      "votes": 11139,
      "colors": {
        "poster": [
          "#AA705E",
          "#1B1F1B"
        ]
      },
      "ending": null,
      "genres": [
        "drama",
        "crime"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/shows/228595/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/shows/228595/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/shows/228595/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/shows/228595/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/shows/228595/medium/poster.webp"
        ],
        "clearart": [
          "media.trakt.tv/shows/228595/medium/clearart.webp"
        ]
      },
      "rating": 8.576003074645996,
      "status": "returning series",
      "country": "us",
      "network": "Paramount+",
      "runtime": 60,
      "tagline": "Chaos reigns.",
      "trailer": "https://youtube.com/watch?v=qKGgw7Ob5f4",
      "homepage": "https://www.paramountplus.com/shows/mobland",
      "language": "en",
      "overview": "Two mob families clash in a war that threatens to topple empires and lives.",
      "languages": [
        "af",
        "en"
      ],
      "subgenres": [
        "gangster",
        "organized-crime",
        "psychological-drama"
      ],
      "last_aired": "2026-11-13T08:00:00.000Z",
      "social_ids": {
        "twitter": "MobLand_PPlus",
        "facebook": null,
        "instagram": "mobland_pplus",
        "wikipedia": "MobLand"
      },
      "updated_at": "2026-11-07T16:13:47.000Z",
      "first_aired": "2025-03-30T07:00:00.000Z",
      "certification": "TV-MA",
      "comment_count": 76,
      "total_runtime": 877,
      "aired_episodes": 19,
      "original_title": "MobLand",
      "available_translations": [
        "ar",
        "bg",
        "cs",
        "da",
        "de",
        "el",
        "en",
        "es",
        "et",
        "fa",
        "fi",
        "fr",
        "he",
        "hr",
        "hu",
        "it",
        "ja",
        "ka",
        "ko",
        "lt",
        "mk",
        "nl",
        "pl",
        "pt",
        "ro",
        "ru",
        "sk",
        "sl",
        "sq",
        "sr",
        "sv",
        "th",
        "tr",
        "uk",
        "vi",
        "zh"
      ]
    }
  }
]"""

class TrendingMediaResponseTest {
    @Test
    fun test() {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }


        assert(true)

        assertDoesNotThrow { jsonConfig.decodeFromString<List<TrendingMediaResponse>>(JSON) }
    }
}