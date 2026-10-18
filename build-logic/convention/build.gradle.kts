plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "com.snac.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("compose") {
            id = "com.snac.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        register("featureApi") {
            id = "com.snac.feature.api"
            implementationClass = "FeatureApiConventionPlugin"
        }

        register("featureImpl") {
            id = "com.snac.feature.impl"
            implementationClass = "FeatureImplConventionPlugin"
        }

        register("featureLibrary") {
            id = "com.snac.feature.library"
            implementationClass = "FeatureLibraryConventionPlugin"
        }
    }
}
