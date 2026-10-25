plugins {
    id("com.snac.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.omasyo.snac.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.network)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
