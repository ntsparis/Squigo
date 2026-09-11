plugins {
  id("squigo.kotlin.multiplatform")
  id("squigo.compose.multiplatform")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":core:data"))
      implementation(project(":core:domain"))
      implementation(project(":core:model"))
      implementation(project(":core:designsystem"))
      implementation(project(":core:ui"))
      implementation(project(":core:scaffold"))

      implementation(project(":feature:habits"))
      implementation(project(":feature:habit-detail"))
    }
  }
}
