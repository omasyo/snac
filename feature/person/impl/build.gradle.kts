plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.person.impl"
}

dependencies {
    implementation(projects.feature.person.api)
    implementation(projects.feature.media.api)
}
