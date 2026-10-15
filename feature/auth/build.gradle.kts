plugins {
    id("com.snac.feature.library")
}

android {
    namespace = "com.omasyo.snac.feature.auth"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
}