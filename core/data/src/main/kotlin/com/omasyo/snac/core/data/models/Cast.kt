package com.omasyo.snac.core.data.models

data class Person(
    val name: String,
    val slug: String,
    val imageUrl: String?,
)

data class Cast(
    val characters: List<String>,
    val person: Person,
) {
    val character: String = characters.joinToString("/")

    companion object {
        val PreviewSample = List(10) {
            Cast(
                characters = listOf("Victor"),
                person = Person(
                    name = "Oscar Isaac",
                    slug = "person-$it",
                    imageUrl = null,
                ),
            )
        }
    }
}
