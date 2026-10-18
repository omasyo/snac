plugins {
    id("com.snac.feature.impl")
}

android {
    namespace = "com.omasyo.snac.feature.auth.impl"
}

dependencies {
    implementation(projects.feature.auth.api)
    implementation(projects.core.network)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
}
