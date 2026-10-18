plugins {
    id("com.snac.feature.library")
}

android {
    namespace = "com.omasyo.snac.feature.home"
}

dependencies {
    implementation(projects.feature.auth.api)
}