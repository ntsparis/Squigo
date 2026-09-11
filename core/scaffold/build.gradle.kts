plugins {
  id("squigo.kotlin.multiplatform")
  id("squigo.compose.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":core:designsystem"))
    }
  }
}
