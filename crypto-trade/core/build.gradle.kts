plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.cor)

    implementation(project(":common"))
    implementation(project(":stubs"))

    api(libs.coroutines.test)
    testImplementation(kotlin("test-junit"))
    testImplementation(projects.repoTests)
    testImplementation(projects.repoInmemory)
}
