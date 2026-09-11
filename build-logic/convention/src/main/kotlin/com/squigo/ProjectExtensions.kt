package com.squigo

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: LibrariesForLibs get() = extensions.getByType()

internal fun Project.moduleNamespace(): String {
  val moduleName = path.split(":").drop(1).joinToString(".") { it.replace("-", ".") }
  return if (moduleName.isNotEmpty()) "com.squigo.$moduleName" else "com.squigo"
}
