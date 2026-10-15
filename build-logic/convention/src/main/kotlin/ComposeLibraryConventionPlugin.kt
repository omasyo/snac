import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                apply(libs.findPlugin("kotlin.compose").get().get().pluginId)
            }

            extensions.getByType<LibraryExtension>().apply {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                val composeBom = platform(libs.findLibrary("androidx.compose.bom").get())
                "implementation"(composeBom)
                "implementation"(libs.findLibrary("androidx.compose.foundation").get())
                "implementation"(libs.findLibrary("androidx.compose.material3").get())
                "implementation"(libs.findLibrary("androidx.compose.ui").get())
                "implementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodelCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodelNavigation3").get())
                "implementation"(libs.findLibrary("androidx.navigation3.ui").get())

                "implementation"(libs.findLibrary("coil.compose").get())
                "implementation"(libs.findLibrary("coil.network.ktor3").get())
                "implementation"(libs.findLibrary("ktor.okhttp").get())

                "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
            }
        }
    }
}
