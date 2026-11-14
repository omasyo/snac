plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.media.impl"
}

dependencies {
    implementation(projects.feature.media.api)
    implementation(projects.feature.person.api)
}
