package com.omasyo.snac.core.network.calendar.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

private const val JSON = """[
  {
    "movie": {
      "ids": {
        "imdb": "tt38686316",
        "plex": {
          "guid": "68f9e41c287b1bab5515ad48",
          "slug": "archangel-2026-1"
        },
        "slug": "ghost-soldier-2026",
        "tmdb": 1562642,
        "trakt": 1289024
      },
      "year": 2026,
      "title": "Ghost Soldier",
      "votes": 138,
      "colors": {
        "poster": [
          "#D6D6D6",
          "#1F1F1F"
        ]
      },
      "genres": [
        "action",
        "thriller"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/movies/1289024/medium/logo.webp"
        ],
        "thumb": [],
        "banner": [],
        "fanart": [
          "media.trakt.tv/movies/1289024/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/movies/1289024/medium/poster.webp"
        ],
        "clearart": []
      },
      "rating": 8.065218925476074,
      "status": "released",
      "country": "us",
      "runtime": 90,
      "tagline": "Heaven sent. Hell to pay.",
      "trailer": "https://youtube.com/watch?v=UKaWzMf9Ybc",
      "homepage": "https://ghostsoldier.movie",
      "language": "en",
      "overview": "Henry “Fitz” Fitzgerald has spent years living quietly off the grid, hiding from a past few people know anything about. But when a powerful foreign corporation moves into his hometown to seize his neighbors’ land for a massive payday, Fitz is forced to reveal who he really is: a former Special Forces soldier and advanced weapons developer. Armed with experimental technology, lethal combat skills, and an unwavering code of justice, Fitz wages a one-man war against an enemy that never saw him coming.",
      "released": "2026-11-11",
      "languages": [
        "en"
      ],
      "subgenres": [
        "one-man-army"
      ],
      "social_ids": {
        "twitter": null,
        "facebook": null,
        "instagram": "ghostsoldiermovie",
        "wikipedia": "Archangel_(2026_film)"
      },
      "updated_at": "2026-11-12T13:39:10.000Z",
      "after_credits": false,
      "certification": "PG-13",
      "comment_count": 5,
      "during_credits": false,
      "original_title": "Ghost Soldier",
      "available_translations": [
        "az",
        "bg",
        "en",
        "es",
        "fr",
        "hu",
        "pl",
        "pt",
        "tr",
        "uk",
        "zh"
      ]
    },
    "released": "2026-11-11"
  },
  {
    "show": {
      "ids": {
        "imdb": "tt0072584",
        "plex": {
          "guid": "65099110021b398de2b089fa",
          "slug": "wheel-of-fortune-5"
        },
        "slug": "wheel-of-fortune",
        "tmdb": 2778,
        "tvdb": 76573,
        "trakt": 2760
      },
      "airs": {
        "day": "Saturday",
        "time": "19:00",
        "timezone": "America/New_York"
      },
      "year": 1983,
      "title": "Wheel of Fortune",
      "votes": 102,
      "colors": {
        "poster": [
          "#A1A9A9",
          "#2A2A27"
        ]
      },
      "ending": null,
      "genres": [
        "family",
        "game-show",
        "reality"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/shows/2760/medium/logo.webp"
        ],
        "thumb": [],
        "banner": [
          "media.trakt.tv/shows/2760/medium/banner.webp"
        ],
        "fanart": [
          "media.trakt.tv/shows/2760/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/shows/2760/medium/poster.webp"
        ],
        "clearart": []
      },
      "rating": 6.421568393707275,
      "status": "returning series",
      "country": "us",
      "network": "Syndication",
      "runtime": 25,
      "tagline": "",
      "trailer": "https://youtube.com/watch?v=3tpMgKHAQDI",
      "homepage": "https://www.wheeloffortune.com/",
      "language": "en",
      "overview": "This game show sees contestants solve word puzzles, similar to those used in Hangman, to win cash and prizes determined by spinning a giant carnival wheel.",
      "languages": [
        "en"
      ],
      "subgenres": [
        "game-show",
        "quiz-show"
      ],
      "last_aired": "2026-11-14T00:00:00.000Z",
      "social_ids": {
        "twitter": null,
        "facebook": null,
        "instagram": null,
        "wikipedia": "Wheel_of_Fortune_(American_game_show)"
      },
      "updated_at": "2026-11-07T16:37:28.000Z",
      "first_aired": "1983-09-19T23:00:00.000Z",
      "certification": "TV-G",
      "comment_count": 0,
      "total_runtime": 166397,
      "aired_episodes": 6658,
      "original_title": "Wheel of Fortune",
      "available_translations": [
        "en",
        "fr",
        "ko",
        "pt",
        "ru",
        "uk"
      ]
    },
    "episode": {
      "ids": {
        "imdb": null,
        "plex": {
          "guid": "6aef187babfc96c1cbd1233c"
        },
        "tmdb": 8086015,
        "tvdb": 12075256,
        "trakt": 14628820
      },
      "title": "Live More Floridays",
      "votes": 4,
      "images": {
        "screenshot": []
      },
      "number": 41,
      "rating": 7.25,
      "season": 44,
      "runtime": 20,
      "overview": null,
      "released": "2026-11-10",
      "number_abs": 6653,
      "updated_at": "2026-11-11T02:50:38.000Z",
      "first_aired": "2026-11-10T00:00:00.000Z",
      "episode_type": "standard",
      "after_credits": false,
      "comment_count": 0,
      "during_credits": false,
      "original_title": "Live More Floridays",
      "available_translations": [
        "en"
      ],
      "effective_release_date": "2026-11-10T00:00:00.000Z"
    },
    "first_aired": "2026-11-10T00:00:00.000Z",
    "released": "2026-11-10"
  },
  {
    "show": {
      "ids": {
        "imdb": "tt27995115",
        "plex": {
          "guid": "64f07dc37805645116362399",
          "slug": "the-devils-plan-1"
        },
        "slug": "the-devil-s-plan",
        "tmdb": 214582,
        "tvdb": 433304,
        "trakt": 208746
      },
      "airs": {
        "day": "Tuesday",
        "time": "16:00",
        "timezone": "Asia/Seoul"
      },
      "year": 2023,
      "title": "The Devil's Plan",
      "votes": 984,
      "colors": {
        "poster": [
          "#838089",
          "#1E120D"
        ]
      },
      "ending": null,
      "genres": [
        "reality",
        "game-show"
      ],
      "images": {
        "logo": [
          "media.trakt.tv/shows/208746/medium/logo.webp"
        ],
        "thumb": [
          "media.trakt.tv/shows/208746/medium/thumb.webp"
        ],
        "banner": [],
        "fanart": [
          "media.trakt.tv/shows/208746/medium/fanart.webp"
        ],
        "poster": [
          "media.trakt.tv/shows/208746/medium/poster.webp"
        ],
        "clearart": []
      },
      "rating": 8.278456687927246,
      "status": "returning series",
      "country": "kr",
      "network": "Netflix",
      "runtime": 80,
      "tagline": "The return. The real showdown",
      "trailer": "https://youtube.com/watch?v=dtaLR2G-zmc",
      "homepage": "https://netflix.com/title/81653386",
      "language": "ko",
      "overview": "It's the ultimate showdown of wits, brains, strategy, and alliances for a chance to win 500 million won.",
      "languages": [
        "en",
        "ko"
      ],
      "subgenres": [
        "game-show",
        "survival-show",
        "talent-show"
      ],
      "last_aired": "2026-11-10T07:00:00.000Z",
      "social_ids": {
        "twitter": null,
        "facebook": null,
        "instagram": null,
        "wikipedia": "The_Devil's_Plan"
      },
      "updated_at": "2026-11-10T18:55:48.000Z",
      "first_aired": "2023-09-26T07:00:00.000Z",
      "certification": "TV-14",
      "comment_count": 9,
      "total_runtime": 2272,
      "aired_episodes": 29,
      "original_title": "데블스 플랜",
      "available_translations": [
        "ar",
        "cs",
        "de",
        "el",
        "en",
        "es",
        "fi",
        "fr",
        "he",
        "hu",
        "it",
        "ja",
        "ko",
        "my",
        "nl",
        "pl",
        "pt",
        "ro",
        "ru",
        "th",
        "tr",
        "uk",
        "vi",
        "zh"
      ]
    },
    "episode": {
      "ids": {
        "imdb": "tt39387649",
        "plex": {
          "guid": "6ac8ff52a099cbcea568d1d6"
        },
        "tmdb": 7924460,
        "tvdb": 12032814,
        "trakt": 14556246
      },
      "title": "TBA",
      "votes": 48,
      "images": {
        "screenshot": []
      },
      "number": 1,
      "rating": 8.479167938232422,
      "season": 3,
      "runtime": 69,
      "overview": "Returning contenders and new faces gather for a showdown of intellect. With high stakes, seasoned competitors with formidable careers take the stage.",
      "released": "2026-11-10",
      "number_abs": 25,
      "updated_at": "2026-11-10T18:55:48.000Z",
      "first_aired": "2026-11-10T07:00:00.000Z",
      "episode_type": "multiple_episodes",
      "after_credits": false,
      "comment_count": 4,
      "during_credits": false,
      "original_title": null,
      "available_translations": [
        "en",
        "ko"
      ],
      "effective_release_date": "2026-11-10T07:00:00.000Z",
      "episodes": [
        {
          "ids": {
            "imdb": "tt39387649",
            "plex": {
              "guid": "6ac8ff52a099cbcea568d1d6"
            },
            "tmdb": 7924460,
            "tvdb": 12032814,
            "trakt": 14556246
          },
          "title": "TBA",
          "votes": 48,
          "images": {
            "screenshot": []
          },
          "number": 1,
          "rating": 8.479167938232422,
          "season": 3,
          "runtime": 69,
          "overview": "Returning contenders and new faces gather for a showdown of intellect. With high stakes, seasoned competitors with formidable careers take the stage.",
          "released": "2026-11-10",
          "number_abs": 25,
          "updated_at": "2026-11-10T18:55:48.000Z",
          "first_aired": "2026-11-10T07:00:00.000Z",
          "episode_type": "season_premiere",
          "after_credits": false,
          "comment_count": 4,
          "during_credits": false,
          "original_title": null,
          "available_translations": [
            "en",
            "ko"
          ],
          "effective_release_date": "2026-11-10T07:00:00.000Z"
        },
        {
          "ids": {
            "imdb": null,
            "plex": {
              "guid": "6ac8ff52a099cbcea568d1c7"
            },
            "tmdb": 7924461,
            "tvdb": 12078168,
            "trakt": 14556247
          },
          "title": "Episode 2",
          "votes": 36,
          "images": {
            "screenshot": []
          },
          "number": 2,
          "rating": 8.333334922790527,
          "season": 3,
          "runtime": 86,
          "overview": "The first elimination comes in the main match. Then, before anyone has time to recover, the prison match begins to decide who goes next.",
          "released": "2026-11-10",
          "number_abs": 26,
          "updated_at": "2026-11-10T18:55:48.000Z",
          "first_aired": "2026-11-10T07:00:00.000Z",
          "episode_type": "standard",
          "after_credits": false,
          "comment_count": 0,
          "during_credits": false,
          "original_title": null,
          "available_translations": [
            "en",
            "ko"
          ],
          "effective_release_date": "2026-11-10T07:00:00.000Z"
        },
        {
          "ids": {
            "imdb": null,
            "plex": {
              "guid": "6af068450a1c39d0b20bae68"
            },
            "tmdb": 8097425,
            "tvdb": 12078169,
            "trakt": 14638590
          },
          "title": "Episode 3",
          "votes": 30,
          "images": {
            "screenshot": []
          },
          "number": 3,
          "rating": 8.533333778381348,
          "season": 3,
          "runtime": 74,
          "overview": "A strong contender is eliminated in the prison match, leaving everyone stunned. From the very first night, an uneasy tension fills the living quarters.",
          "released": "2026-11-10",
          "number_abs": 27,
          "updated_at": "2026-11-10T14:49:45.000Z",
          "first_aired": "2026-11-10T07:00:00.000Z",
          "episode_type": "standard",
          "after_credits": false,
          "comment_count": 2,
          "during_credits": false,
          "original_title": null,
          "available_translations": [
            "en"
          ],
          "effective_release_date": "2026-11-10T07:00:00.000Z"
        },
        {
          "ids": {
            "imdb": null,
            "plex": {
              "guid": "6af068450a1c39d0b20bae59"
            },
            "tmdb": 8097428,
            "tvdb": 12078170,
            "trakt": 14638591
          },
          "title": "Episode 4",
          "votes": 25,
          "images": {
            "screenshot": []
          },
          "number": 4,
          "rating": 8.719999313354492,
          "season": 3,
          "runtime": 88,
          "overview": null,
          "released": "2026-11-10",
          "number_abs": 28,
          "updated_at": "2026-11-10T14:49:46.000Z",
          "first_aired": "2026-11-10T07:00:00.000Z",
          "episode_type": "standard",
          "after_credits": false,
          "comment_count": 0,
          "during_credits": false,
          "original_title": null,
          "available_translations": [
            "ko"
          ],
          "effective_release_date": "2026-11-10T07:00:00.000Z"
        },
        {
          "ids": {
            "imdb": null,
            "plex": {
              "guid": "6af068440a1c39d0b20bae4a"
            },
            "tmdb": 8097432,
            "tvdb": 12078171,
            "trakt": 14638592
          },
          "title": "Episode 5",
          "votes": 21,
          "images": {
            "screenshot": []
          },
          "number": 5,
          "rating": 8.714284896850586,
          "season": 3,
          "runtime": 75,
          "overview": "Tears flow as an unexpected elimination leaves players stunned. Then a contentious prison match sends even more players home.",
          "released": "2026-11-10",
          "number_abs": 29,
          "updated_at": "2026-11-11T07:24:55.000Z",
          "first_aired": "2026-11-10T07:00:00.000Z",
          "episode_type": "standard",
          "after_credits": false,
          "comment_count": 0,
          "during_credits": false,
          "original_title": null,
          "available_translations": [
            "en",
            "ko"
          ],
          "effective_release_date": "2026-11-10T07:00:00.000Z"
        }
      ]
    },
    "first_aired": "2026-11-10T07:00:00.000Z",
    "released": "2026-11-10"
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
        val movie = result[0] as CalendarMovieResponse
        assertEquals(listOf("media.trakt.tv/movies/1289024/medium/poster.webp"), movie.movie.images?.poster)

        val single = result[1] as CalendarShowResponse
        assertNull(single.episode.episodes)

        val multi = result[2] as CalendarShowResponse
        assertEquals("multiple_episodes", multi.episode.episodeType)
        assertEquals(5, multi.episode.episodes?.size)
        assertEquals("Episode 2", multi.episode.episodes?.get(1)?.title)
    }
}
