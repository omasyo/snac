import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeatureLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                apply("com.snac.android.library")
                apply("com.snac.compose.library")
                apply(libs.findPlugin("kotlin.plugin.serialization").get().get().pluginId)
                apply(libs.findPlugin("metro").get().get().pluginId)
            }

            dependencies {
                "implementation"(project(":core:ui"))

                "implementation"(libs.findLibrary("kotlinx.serialization.core").get())
                "implementation"(libs.findLibrary("metrox.viewmodel.compose").get())
            }
        }
    }
}
