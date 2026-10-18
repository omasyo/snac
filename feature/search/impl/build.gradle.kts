plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.search.impl"
}

dependencies {
    implementation(projects.feature.search.api)
}
