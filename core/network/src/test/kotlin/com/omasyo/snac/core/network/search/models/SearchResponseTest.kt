package com.omasyo.snac.core.network.search.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val MOVIES = """[
  {
    "score": 578730123365189600,
    "type": "movie",
    "movie": {
      "ids": {
        "imdb": "tt1104001",
        "plex": {
          "guid": "5d77684561141d001fb17810",
          "slug": "tron-legacy"
        },
        "slug": "tron-legacy-2010",
        "tmdb": 20526,
        "trakt": 12601
      },
      "year": 2010,
      "title": "TRON: Legacy",
      "votes": 18310,
      "colors": {
        "poster": [
          "#59AFBB",
          "#062C41"
        ]
      },
      "genres": [
        "action",
        "adventure",
        "science-fiction"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/movies/12601/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/movies/12601/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/movies/12601/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/movies/12601/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/movies/12601/medium/poster.webp"
        ],
        "clearart": [
          "media.trakt.tv/movies/12601/medium/clearart.webp"
        ]
      },
      "rating": 7.179736614227295,
      "status": "released",
      "country": "us",
      "runtime": 126,
      "tagline": "The game has changed.",
      "trailer": "https://youtube.com/watch?v=m9CTGwEZiPk",
      "homepage": "https://movies.disney.com/tron-legacy",
      "language": "en",
      "overview": "Sam Flynn, the tech-savvy and daring son of Kevin Flynn, investigates his father's disappearance and is pulled into The Grid. With the help of a mysterious program named Quorra, Sam quests to stop evil dictator Clu from crossing into the real world.",
      "released": "2010-12-17",
      "languages": [
        "en"
      ],
      "subgenres": [
        "dystopia",
        "epic",
        "cyberpunk",
        "artificial-intelligence-a-i"
      ],
      "social_ids": {
        "twitter": "TronLegacy",
        "facebook": "Tron",
        "instagram": null,
        "wikipedia": "Tron:_Legacy"
      },
      "updated_at": "2026-11-13T06:34:37.000Z",
      "after_credits": false,
      "certification": "PG",
      "comment_count": 88,
      "during_credits": false,
      "original_title": "TRON: Legacy",
      "available_translations": [
        "ar",
        "bg",
        "ca",
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
        "hi",
        "hr",
        "hu",
        "it",
        "ja",
        "ka",
        "ko",
        "lt",
        "lv",
        "nl",
        "no",
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
        "vi",
        "zh"
      ]
    }
  }
]"""

private const val SHOWS = """[
  {
    "score": 578730123365189600,
    "type": "show",
    "show": {
      "ids": {
        "imdb": "tt0903747",
        "plex": {
          "guid": "5d9c083402391c001f583d09",
          "slug": "breaking-bad"
        },
        "slug": "breaking-bad",
        "tmdb": 1396,
        "tvdb": 81189,
        "trakt": 1388
      },
      "airs": {
        "day": "Sunday",
        "time": "21:00",
        "timezone": "America/New_York"
      },
      "year": 2008,
      "title": "Breaking Bad",
      "votes": 75258,
      "colors": {
        "poster": [
          "#A5AF96",
          "#272A2E"
        ]
      },
      "ending": "concluded",
      "genres": [
        "drama",
        "crime",
        "thriller"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/shows/1388/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/shows/1388/medium/thumb.webp"
        ],
        "banner": [
          "media.trakt.tv/shows/1388/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/shows/1388/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/shows/1388/medium/poster.webp"
        ],
        "clearart": [
          "media.trakt.tv/shows/1388/medium/clearart.webp"
        ]
      },
      "rating": 9.318061828613281,
      "status": "ended",
      "country": "us",
      "network": "AMC",
      "runtime": 50,
      "tagline": "Change the equation.",
      "trailer": "https://youtube.com/watch?v=VFkjBy2b50Q",
      "homepage": "https://www.sonypictures.com/tv/breakingbad",
      "language": "en",
      "overview": "Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
      "languages": [
        "en",
        "de",
        "es"
      ],
      "subgenres": [
        "dark-comedy",
        "psychological-thriller",
        "psychological-drama",
        "tragedy",
        "neo-western",
        "drug-crime"
      ],
      "last_aired": "2013-09-30T01:00:00.000Z",
      "social_ids": {
        "twitter": "BreakingBad",
        "facebook": "BreakingBad",
        "instagram": "breakingbad",
        "wikipedia": "Breaking_Bad"
      },
      "updated_at": "2026-11-07T15:18:30.000Z",
      "first_aired": "2008-01-21T02:00:00.000Z",
      "certification": "TV-MA",
      "comment_count": 584,
      "total_runtime": 2995,
      "aired_episodes": 62,
      "original_title": "Breaking Bad",
      "available_translations": [
        "ar",
        "az",
        "be",
        "bg",
        "bn",
        "bs",
        "ca",
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
        "hi",
        "hr",
        "hu",
        "hy",
        "id",
        "it",
        "ja",
        "ka",
        "kk",
        "kn",
        "ko",
        "ku",
        "ky",
        "lt",
        "lv",
        "mk",
        "ml",
        "ne",
        "nl",
        "no",
        "pa",
        "pl",
        "pt",
        "ro",
        "ru",
        "si",
        "sk",
        "sl",
        "sr",
        "sv",
        "ta",
        "th",
        "tr",
        "tt",
        "ug",
        "uk",
        "ur",
        "vi",
        "zh"
      ]
    }
  }
]"""

private const val PEOPLE = """[
  {
    "score": 578730123365189800,
    "type": "person",
    "person": {
      "ids": {
        "imdb": "nm0415777",
        "slug": "bryan-leung-kar-yan",
        "tmdb": 227782,
        "trakt": 422884
      },
      "name": "Bryan Leung Kar-yan",
      "death": null,
      "gender": "male",
      "height": null,
      "images": {
        "fanart": [],
        "headshot": [
          "media.trakt.tv/people/422884/medium/headshot.webp"
        ]
      },
      "summary": "Bryan Leung is a Hong Kong actor known for his roles in martial arts films like Ip Man 3 and The Man with the Iron Fists. He is recognized for his trademark facial hair and hyper-extendable fingers, despite having no formal martial arts training.",
      "birthday": "1949-01-20",
      "homepage": null,
      "biography": "Bryan Leung is a Hong Kong film and television actor and director, affectionately known as \"Beardy\" for his trademark facial hair and notable for his hyper-extendable fingers. His action film career began in 1969, leading to roles in numerous acclaimed martial arts films. He had a big break under Sammo Hung, co-starring in Warriors Two, a performance described as the best of his career. He continued to achieve success in kung fu comedies and collaborated with director Yuen Woo-ping on films like Legend of a Fighter. Leung also had a notable television career before making his directorial debut in 1984. He has appeared in high-profile films such as Ip Man 3 and The Man with the Iron Fists.",
      "birthplace": "Hong Kong, British Crown Colony [now China]",
      "social_ids": {
        "tiktok": null,
        "twitter": null,
        "youtube": null,
        "facebook": null,
        "wikidata": "Q860948",
        "instagram": null,
        "wikipedia": "Bryan_Leung",
        "wikipedia_language": "en"
      },
      "updated_at": "2026-11-02T08:04:04.000Z",
      "known_for_department": "acting"
    }
  }
]"""

class SearchResponseTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `decodes movie results`() {
        val movie = json.decodeFromString<List<MovieSearchResponse>>(MOVIES).single()
        assertEquals("tron-legacy-2010", movie.movie.ids.slug)
        assertEquals(5.787301233651896e+17, movie.score)
    }

    @Test
    fun `decodes show results`() {
        val show = json.decodeFromString<List<ShowSearchResponse>>(SHOWS).single()
        assertEquals("Breaking Bad", show.show.title)
    }

    @Test
    fun `decodes person results`() {
        val person = json.decodeFromString<List<PersonSearchResponse>>(PEOPLE).single()
        assertEquals("bryan-leung-kar-yan", person.person.ids.slug)
        assertEquals(
            listOf("media.trakt.tv/people/422884/medium/headshot.webp"),
            person.person.images?.headshot,
        )
    }
}
