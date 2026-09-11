pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Squigo"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":android")

include(":core:data")
include(":core:database")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:scaffold")
include(":core:ui")

include(":feature:habits")
include(":feature:habit-detail")
