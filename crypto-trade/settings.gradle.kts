rootProject.name = "crypto-trade"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../plugins")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":common")
include(":api-v1")
include(":log-v1")
include(":core")
include(":app-common")
include(":app-ktor")
include(":app-kafka")
include(":mappers")
include(":stubs")

// DB
include(":repo-common")
include(":repo-inmemory")
include(":repo-stubs")
include(":repo-tests")
include(":repo-cassandra")

// Auth
include(":auth")