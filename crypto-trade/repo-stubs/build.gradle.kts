plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(libs.coroutines.core)
    implementation(kotlin("stdlib"))
    implementation(projects.common)
    implementation(projects.stubs)

    testImplementation(kotlin("test-junit"))
}
