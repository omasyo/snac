plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.charts.impl"
}

dependencies {
    implementation(projects.feature.charts.api)
    implementation(projects.feature.media.api)
}
