plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.cryptotrade.libs:logging-common")
}
