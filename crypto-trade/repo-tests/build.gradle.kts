plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(libs.coroutines.core)
    api(libs.coroutines.test)
    api(kotlin("test-junit"))
    implementation(projects.common)
    implementation(projects.repoCommon)
    implementation(kotlin("stdlib"))

    testImplementation(projects.stubs)
}
