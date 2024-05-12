plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}
