package com.omasyo.snac.core.network.search.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val MOVIES = """[
  {
    "score": 578730089005451300,
    "type": "movie",
    "movie": {
      "ids": {
        "imdb": "tt15678738",
        "plex": { "guid": "616a96cd039aabdb6c7571dc", "slug": "david-2025" },
        "slug": "david-2025",
        "tmdb": 1167307,
        "trakt": 940730
      },
      "year": 2025,
      "title": "David",
      "released": "2025-12-19"
    }
  }
]"""

private const val SHOWS = """[
  {
    "score": 578730089005451300,
    "type": "show",
    "show": {
      "ids": {
        "imdb": "tt8531222",
        "plex": { "guid": "5d9c09113c3f87001f35b8e3", "slug": "dave" },
        "slug": "dave",
        "tmdb": 97084,
        "tvdb": 354905,
        "trakt": 157190
      },
      "airs": { "day": "Weekdays", "time": null, "timezone": "America/New_York" },
      "year": 2020,
      "title": "Dave",
      "aired_episodes": 30
    }
  }
]"""

private const val PEOPLE = """[
  {
    "score": 578730089005451400,
    "type": "person",
    "person": {
      "ids": {
        "imdb": "nm0202515",
        "slug": "charlie-davao",
        "tmdb": 295406,
        "trakt": 186192
      },
      "name": "Charlie Davao",
      "death": "2010-08-08",
      "gender": "male",
      "height": null,
      "images": {
        "fanart": [],
        "headshot": [ "media.trakt.tv/people/186192/medium/headshot.webp" ]
      },
      "summary": null,
      "biography": null,
      "birthplace": null
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
        assertEquals("david-2025", movie.movie.ids.slug)
        assertEquals(578730089005451300.0, movie.score)
    }

    @Test
    fun `decodes show results`() {
        val show = json.decodeFromString<List<ShowSearchResponse>>(SHOWS).single()
        assertEquals("Dave", show.show.title)
    }

    @Test
    fun `decodes person results`() {
        val person = json.decodeFromString<List<PersonSearchResponse>>(PEOPLE).single()
        assertEquals("charlie-davao", person.person.ids.slug)
        assertEquals(
            listOf("media.trakt.tv/people/186192/medium/headshot.webp"),
            person.person.images?.headshot,
        )
    }
}
