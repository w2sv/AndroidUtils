@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":androidutils-core")
include(":androidutils-junit5")
include(":androidutils-lifecycle")
include(":androidutils-view")

rootProject.name = "AndroidUtils"
