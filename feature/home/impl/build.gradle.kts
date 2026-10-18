plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.home.impl"
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.feature.auth.api)
}
