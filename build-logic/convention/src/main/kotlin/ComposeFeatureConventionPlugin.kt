import com.squigo.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply {
      apply("org.jetbrains.compose")
      apply("org.jetbrains.kotlin.plugin.compose")
    }

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        commonMain.dependencies {
          implementation(project(":core:ui"))
          implementation(project(":core:designsystem"))
          implementation(project(":core:scaffold"))

          implementation(libs.koin.compose)
          implementation(libs.koin.compose.viewmodel)
          implementation(libs.navigation.compose)
        }
      }
    }
  }
}
