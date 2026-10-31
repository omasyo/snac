package com.omasyo.snac.core.data.models

data class Person(
    val name: String,
    val slug: String,
    val imageUrl: String?,
)

data class Cast(
    val character: String,
    val person: Person,
) {
    companion object {
        val PreviewSample = List(10) {
            Cast(
                character = "Victor",
                person = Person(
                    name = "Oscar Isaac",
                    slug = "person-$it",
                    imageUrl = null,
                ),
            )
        }
    }
}
