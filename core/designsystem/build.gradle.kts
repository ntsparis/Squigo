plugins {
  id("squigo.kotlin.multiplatform")
  id("squigo.compose.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(project(":core:model"))
    }
  }
}
