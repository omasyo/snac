plugins {
    id("com.snac.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.omasyo.snac.core.network"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.core)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.serialization.json)
}
