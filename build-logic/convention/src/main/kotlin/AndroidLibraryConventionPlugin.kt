import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
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

    dependencies {
        "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
    }
}
