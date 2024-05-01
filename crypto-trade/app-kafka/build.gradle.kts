plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.crypto.trade.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.otus.otuskotlin.cryptotrade.libs:logging-logback")

    implementation(project(":app-common"))

    // transport models
    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":mappers"))
    // logic
    implementation(project(":core"))

    testImplementation(kotlin("test-junit"))
}
