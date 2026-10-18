import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                apply("com.snac.android.library")
                apply(libs.findPlugin("kotlin.plugin.serialization").get().get().pluginId)
            }

            dependencies {
                "implementation"(libs.findLibrary("kotlinx.serialization.core").get())

                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
            }
        }
    }
}
