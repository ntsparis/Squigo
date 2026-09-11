plugins {
  id("squigo.kotlin.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(project(":core:domain"))
      implementation(project(":core:database"))
      implementation(project(":core:model"))
      implementation(libs.kotlinx.datetime)
    }
  }
}
