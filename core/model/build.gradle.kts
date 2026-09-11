plugins {
  id("squigo.kotlin.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.datetime)
    }
  }
}
