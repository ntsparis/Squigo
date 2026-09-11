import com.squigo.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformPlugin : Plugin<Project> {

  override fun apply(target: Project): Unit = with(target) {
    pluginManager.apply {
      apply("com.android.kotlin.multiplatform.library")
      apply("org.jetbrains.kotlin.multiplatform")
      apply("org.jetbrains.kotlin.plugin.serialization")
    }

    extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
  }
}
