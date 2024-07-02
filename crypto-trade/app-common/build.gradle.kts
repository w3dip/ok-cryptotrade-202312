plugins {
    id("build-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":log-v1"))
    implementation(project(":core"))
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.coroutines.test)
    testImplementation(project(":api-v1"))
    testImplementation(project(":mappers"))
    testImplementation(kotlin("test-junit"))
}