package com.squigo

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal fun KotlinMultiplatformExtension.androidLibrary(
  block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit,
) {
  configure<KotlinMultiplatformAndroidLibraryTarget>(block)
}

internal fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformExtension) = extension.apply {
  jvmToolchain(21)

  sourceSets.all {
    languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    languageSettings.optIn("kotlin.time.ExperimentalTime")
  }

  targets
    .withType<KotlinNativeTarget>()
    .matching { it.konanTarget.family.isAppleFamily }
    .configureEach {
      binaries {
        framework {
          baseName = "shared"
          isStatic = true
        }
      }
    }

  tasks
    .withType<AbstractTestTask>()
    .configureEach {
      failOnNoDiscoveredTests.set(false)
    }

  androidLibrary {
    namespace = moduleNamespace()

    minSdk = libs.versions.min.sdk.get().toInt()
    compileSdk = libs.versions.compile.sdk.get().toInt()

    androidResources { enable = true }

    withHostTest {}
  }
  iosArm64()
  iosX64()
  iosSimulatorArm64()

  applyDefaultHierarchyTemplate()

  sourceSets.apply {
    commonMain.dependencies {
      implementation(libs.koin.core)
      implementation(libs.napier)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.kotlinx.coroutines.core)
    }

    androidMain.dependencies {
      implementation(libs.koin.android)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(libs.kotlinx.coroutines.test)
      implementation(libs.turbine)
    }
  }
}
