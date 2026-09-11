plugins {
  id("squigo.kotlin.multiplatform")
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(project(":core:model"))

      implementation(libs.sqldelight.runtime)
      implementation(libs.sqldelight.coroutines.extensions)
      implementation(libs.kotlinx.datetime)
    }

    androidMain.dependencies {
      implementation(libs.sqldelight.android.driver)
    }

    nativeMain.dependencies {
      implementation(libs.sqldelight.native.driver)
    }
  }
}

sqldelight {
  databases {
    create("SquigoDatabase") {
      packageName.set("com.squigo.core.database")
    }
  }
}
