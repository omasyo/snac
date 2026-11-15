package com.omasyo.snac.core.network.search.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val JSON = """[
  {
    "type": "show",
    "id": 150900,
    "count": 2870,
    "show": {
      "ids": {
        "imdb": "tt7631058",
        "plex": {
          "guid": "5d9c09154eefaa001f5e7cd2",
          "slug": "the-lord-of-the-rings-the-rings-of-power"
        },
        "slug": "the-lord-of-the-rings-the-rings-of-power",
        "tmdb": 84773,
        "tvdb": 367506,
        "trakt": 150900
      },
      "airs": {
        "day": "Thursday",
        "time": "03:00",
        "timezone": "America/New_York"
      },
      "year": 2022,
      "title": "The Lord of the Rings: The Rings of Power",
      "votes": 15310,
      "colors": {
        "poster": [
          "#B1946F",
          "#261E17"
        ]
      },
      "ending": null,
      "genres": [
        "fantasy",
        "action",
        "adventure",
        "drama"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/shows/150900/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/shows/150900/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/shows/150900/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/shows/150900/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/shows/150900/medium/poster.webp"
        ],
        "clearart": [
          "media.trakt.tv/shows/150900/medium/clearart.webp"
        ]
      },
      "rating": 7.206595420837402,
      "status": "returning series",
      "country": "us",
      "network": "Prime Video",
      "runtime": 70,
      "tagline": "Evil has had many names.",
      "trailer": "https://youtube.com/watch?v=x8UAUAuKNcU",
      "homepage": "https://www.amazon.com/dp/B09QHC2LZM",
      "language": "en",
      "overview": "Beginning in a time of relative peace, we follow an ensemble cast of characters as they confront the re-emergence of evil to Middle-earth. From the darkest depths of the Misty Mountains, to the majestic forests of Lindon, to the breathtaking island kingdom of Númenor, to the furthest reaches of the map, these kingdoms and characters will carve out legacies that live on long after they are gone.",
      "languages": [
        "en"
      ],
      "subgenres": [
        "magic",
        "sword-and-sorcery",
        "epic",
        "high-fantasy"
      ],
      "last_aired": "2026-11-11T08:00:00.000Z",
      "social_ids": {
        "twitter": "TheRingsofPower",
        "facebook": "TheRingsOfPower",
        "instagram": "theringsofpower",
        "wikipedia": "The_Lord_of_the_Rings:_The_Rings_of_Power"
      },
      "updated_at": "2026-11-12T09:32:16.000Z",
      "first_aired": "2022-09-02T01:00:00.000Z",
      "certification": "TV-14",
      "comment_count": 160,
      "total_runtime": 1369,
      "aired_episodes": 20,
      "original_title": "The Lord of the Rings: The Rings of Power",
      "available_translations": [
        "ar",
        "az",
        "be",
        "bg",
        "ca",
        "cs",
        "da",
        "de",
        "el",
        "en",
        "es",
        "et",
        "eu",
        "fa",
        "fi",
        "fr",
        "gl",
        "he",
        "hi",
        "hr",
        "hu",
        "id",
        "it",
        "ja",
        "ka",
        "kn",
        "ko",
        "lt",
        "lv",
        "ml",
        "ms",
        "my",
        "nl",
        "no",
        "pl",
        "pt",
        "ro",
        "ru",
        "sk",
        "sl",
        "so",
        "sr",
        "sv",
        "ta",
        "te",
        "th",
        "tl",
        "tr",
        "uk",
        "vi",
        "zh"
      ]
    }
  },
  {
    "type": "movie",
    "id": 1164118,
    "count": 2221,
    "movie": {
      "ids": {
        "imdb": "tt35538033",
        "plex": {
          "guid": "67a23f4861078f9079534716",
          "slug": "resident-evil-2026"
        },
        "slug": "resident-evil-2026",
        "tmdb": 1423191,
        "trakt": 1164118
      },
      "year": 2026,
      "title": "Resident Evil",
      "votes": 14762,
      "colors": {
        "poster": [
          "#BDA9AB",
          "#181417"
        ]
      },
      "genres": [
        "horror",
        "science-fiction",
        "adventure"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/movies/1164118/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/movies/1164118/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/movies/1164118/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/movies/1164118/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/movies/1164118/medium/poster.webp"
        ],
        "clearart": []
      },
      "rating": 7.5476202964782715,
      "status": "released",
      "country": "us",
      "runtime": 94,
      "tagline": "A new era of evil.",
      "trailer": "https://youtube.com/watch?v=mNd1gb19A-c",
      "homepage": "https://residentevil.movie",
      "language": "en",
      "overview": "Medical courier Bryan unwittingly finds himself fighting for survival as one fateful, horrifying night collapses around him in chaos.",
      "released": "2026-09-18",
      "languages": [
        "en"
      ],
      "subgenres": [
        "monster",
        "survival",
        "dark-comedy",
        "survival-horror",
        "zombie",
        "body-horror"
      ],
      "social_ids": {
        "twitter": "ResidentEvil",
        "facebook": "ResidentEvilMovie",
        "instagram": "residentevil",
        "wikipedia": "Resident_Evil_(2026_film)"
      },
      "updated_at": "2026-11-13T04:29:42.000Z",
      "after_credits": false,
      "certification": "R",
      "comment_count": 352,
      "during_credits": false,
      "original_title": "Resident Evil",
      "available_translations": [
        "af",
        "ar",
        "az",
        "be",
        "bg",
        "bn",
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
        "lv",
        "mr",
        "ms",
        "nl",
        "pl",
        "pt",
        "ro",
        "ru",
        "si",
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
        assertEquals(2870, show.count)
        assertEquals("the-lord-of-the-rings-the-rings-of-power", show.show.ids.slug)

        val movie = result[1] as TrendingSearchMovieResponse
        assertEquals(2221, movie.count)
        assertEquals("Resident Evil", movie.movie.title)
    }
}
