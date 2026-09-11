package com.squigo

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(extension: LibraryExtension) {
  extension.apply {
    namespace = moduleNamespace()

    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
      minSdk = libs.versions.min.sdk.get().toInt()
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_21
      targetCompatibility = JavaVersion.VERSION_21
    }

    configureKotlin()
  }
}

internal fun Project.configureApplicationKotlinAndroid(extension: ApplicationExtension) = extension.apply {
  namespace = moduleNamespace()

  defaultConfig.targetSdk = libs.versions.target.sdk.get().toInt()
  defaultConfig.minSdk = libs.versions.min.sdk.get().toInt()
  compileSdk = libs.versions.compile.sdk.get().toInt()

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  configureKotlin()
}

private fun Project.configureKotlin() = configure<KotlinAndroidProjectExtension> {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
    freeCompilerArgs.addAll(
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlin.time.ExperimentalTime",
    )
  }
}
