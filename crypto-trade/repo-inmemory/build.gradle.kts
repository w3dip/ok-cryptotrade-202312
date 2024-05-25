plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    implementation(kotlin("stdlib"))
    api(projects.repoCommon)
    implementation(projects.common)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.repoTests)
}
