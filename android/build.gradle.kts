plugins {
  id("squigo.android.application")
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.compose)
}

android {
  defaultConfig {
    applicationId = "com.squigo.app"
    versionCode = libs.versions.version.code.get().toInt()
    versionName = libs.versions.version.name.get()
  }

  buildFeatures {
    compose = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }
}

dependencies {
  implementation(project(":app"))
  implementation(project(":core:data"))
  implementation(project(":core:domain"))
  implementation(project(":core:model"))
  implementation(project(":core:designsystem"))
  implementation(project(":core:ui"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)

  implementation(libs.koin.android)
  implementation(libs.koin.compose)

  implementation(libs.glance.appwidget)
  implementation(libs.glance.material3)

  implementation(libs.kotlinx.datetime)
}
