plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":log-v1"))
    implementation(project(":core"))

    testImplementation(libs.coroutines.test)
    testImplementation(project(":api-v1"))
    testImplementation(project(":mappers"))
    testImplementation(kotlin("test-junit"))
}