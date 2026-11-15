plugins {
    id("com.snac.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.omasyo.snac.core.navigation"
    buildFeatures.compose = true
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime.saveable)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}
