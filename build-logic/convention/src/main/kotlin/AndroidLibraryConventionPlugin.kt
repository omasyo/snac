import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                apply(libs.findPlugin("android.library").get().get().pluginId)
            }

            configureAndroidLibrary(extensions.getByType<LibraryExtension>())
        }
    }
}


fun Project.configureAndroidLibrary(extension: LibraryExtension) {

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    extension.apply {
        compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()

        defaultConfig {
            minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    dependencies {
        "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
        "testImplementation"(libs.findLibrary("junit.jupiter").get())
        "testImplementation"(libs.findLibrary("junit.platform.launcher").get())
    }
}
