package com.omasyo.snac.core.data.models

import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import java.time.LocalDate

data class PersonDetails(
    val slug: String,
    val name: String,
    val imageUrl: String?,
    val backdropUrl: String?,
    val summary: String?,
    val biography: String?,
    val knownFor: String?,
    val birthday: LocalDate?,
    val death: LocalDate?,
    val birthplace: String?,
) {
    companion object {
        val PreviewSample = PersonDetails(
            slug = "christoph-waltz",
            name = "Christoph Waltz",
            imageUrl = null,
            backdropUrl = null,
            summary = "Two-time Oscar winner with a gift for charming menace.",
            biography = "Austrian actor who spent decades in European television before his international breakthrough as Hans Landa in Inglourious Basterds. He won the Academy Award for Best Supporting Actor for that role and again for Django Unchained. He has since worked with directors including Roman Polanski, Terry Gilliam and Guillermo del Toro, and made his own directing debut with Georgetown.",
            knownFor = "Acting",
            birthday = LocalDate.of(1956, 10, 4),
            death = null,
            birthplace = "Vienna, Austria",
        )
    }
}

enum class CreditRole { Acting, Crew }

data class PersonCredit(
    val media: Media,
    val role: CreditRole,
    /** The character played, or the job held for [CreditRole.Crew]. */
    val description: String,
    val episodeCount: Int? = null,
    val isRegular: Boolean = false,
) {
    companion object {
        val PreviewSample = listOf(
            PersonCredit(
                media = Movie("frankenstein-2025", "Frankenstein", 2025, null),
                role = CreditRole.Acting,
                description = "Heinrich Harlander",
            ),
            PersonCredit(
                media = Show("the-consultant", "The Consultant", 2023, null),
                role = CreditRole.Acting,
                description = "Regus Patoff",
                episodeCount = 8,
                isRegular = true,
            ),
            PersonCredit(
                media = Movie("no-time-to-die-2021", "No Time to Die", 2021, null),
                role = CreditRole.Acting,
                description = "Ernst Stavro Blofeld",
            ),
            PersonCredit(
                media = Movie("georgetown-2019", "Georgetown", 2019, null),
                role = CreditRole.Crew,
                description = "Director",
            ),
            PersonCredit(
                media = Movie("alita-battle-angel-2019", "Alita: Battle Angel", 2019, null),
                role = CreditRole.Acting,
                description = "Dr. Dyson Ido",
            ),
            PersonCredit(
                media = Movie("django-unchained-2012", "Django Unchained", 2012, null),
                role = CreditRole.Acting,
                description = "Dr. King Schultz",
            ),
            PersonCredit(
                media = Movie("inglourious-basterds-2009", "Inglourious Basterds", 2009, null),
                role = CreditRole.Acting,
                description = "Hans Landa",
            ),
        )
    }
}
