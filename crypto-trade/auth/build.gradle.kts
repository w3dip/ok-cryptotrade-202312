plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    api(libs.coroutines.core)
    api(libs.kotlinx.datetime)
    implementation(projects.common)
}
