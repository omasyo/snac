plugins {
    id("com.snac.feature.api")
    id("com.snac.compose")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.omasyo.snac.feature.auth.api"
}

dependencies {
    implementation(libs.metrox.android)
}
