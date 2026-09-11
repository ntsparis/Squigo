import com.squigo.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply {
      apply("org.jetbrains.compose")
      apply("org.jetbrains.kotlin.plugin.compose")
    }

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        commonMain.dependencies {
          api(libs.compose.multiplatform.runtime)
          api(libs.compose.multiplatform.foundation)
          api(libs.compose.multiplatform.material3)
          api(libs.compose.multiplatform.resources)
          api(libs.compose.multiplatform.ui.tooling.preview)
          api(libs.compose.multiplatform.material.icons)

          implementation(libs.lifecycle.multiplatform.viewmodel)
          implementation(libs.lifecycle.multiplatform.viewmodel.compose)
          implementation(libs.lifecycle.multiplatform.runtime)
          implementation(libs.lifecycle.multiplatform.runtime.compose)

          api(libs.kotlinx.serialization.core)
          api(libs.koin.compose)
          api(libs.koin.compose.viewmodel)

          api(libs.navigation.compose)
        }

        androidMain.dependencies {
          implementation(libs.compose.multiplatform.ui.tooling)
        }
      }
    }
  }
}
