plugins {
  id("squigo.kotlin.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(project(":core:model"))
      implementation(libs.kotlinx.datetime)
    }
  }
}
