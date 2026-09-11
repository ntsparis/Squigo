plugins {
  id("squigo.kotlin.multiplatform")
  id("squigo.compose.multiplatform")
  id("squigo.compose.feature")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":core:domain"))
      implementation(project(":core:model"))
      implementation(libs.kotlinx.datetime)
    }
  }
}
