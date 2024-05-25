plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

group = "ru.otus.otuskotlin.crypto.trade"
version = "1.0.0"

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("specs-trade-api-v1", specDir.file("specs-trade-api-v1.yml").toString())
    set("specs-trade-log-v1", specDir.file("specs-trade-log-v1.yml").toString())
}

tasks {
    create("build") {
        group = "build"
        dependsOn(project(":app-ktor").getTasksByName("build", false))
    }
    create("check") {
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@create.dependsOn(it)
            }
        }
    }
}
