plugins {
    alias(libs.plugins.android.application)
    id("com.snac.compose")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.omasyo.snac"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.omasyo.snac"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1"
    }
    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.auth.impl)
    implementation(projects.feature.calendar.api)
    implementation(projects.feature.calendar.impl)
    implementation(projects.feature.charts.api)
    implementation(projects.feature.charts.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)

    implementation(libs.androidx.activity.compose)

    implementation(libs.metrox.android)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)
}
