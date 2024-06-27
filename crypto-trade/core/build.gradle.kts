plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.cor)

    implementation(projects.common)
    implementation(projects.stubs)
    implementation(projects.auth)

    api(libs.coroutines.test)
    testImplementation(kotlin("test-junit"))
    testImplementation(projects.repoTests)
    testImplementation(projects.repoInmemory)
}
