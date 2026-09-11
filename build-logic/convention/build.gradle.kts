import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
}

group = "com.squigo.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
  }
}

dependencies {
  compileOnly(libs.android.gradle.plugin)
  compileOnly(libs.android.tools.common)
  compileOnly(libs.compose.gradle.plugin)
  compileOnly(libs.compose.multiplatform.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin)

  // https://github.com/gradle/gradle/issues/15383
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "squigo.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidFeature") {
      id = "squigo.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("kotlinMultiplatform") {
      id = "squigo.kotlin.multiplatform"
      implementationClass = "KotlinMultiplatformPlugin"
    }
    register("composeMultiplatform") {
      id = "squigo.compose.multiplatform"
      implementationClass = "ComposeMultiplatformPlugin"
    }
    register("composeFeature") {
      id = "squigo.compose.feature"
      implementationClass = "ComposeFeatureConventionPlugin"
    }
  }
}
