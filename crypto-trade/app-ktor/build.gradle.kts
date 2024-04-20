plugins {
    id("build-jvm")
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.yaml)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.server.headers.default)
    implementation(libs.ktor.server.websocket)

    implementation(libs.logback)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.ktor.client.negotiation)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.coroutines.test)

    implementation(project(":app-common"))
    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":log-v1"))
    implementation(project(":core"))
    implementation(project(":mappers"))
    implementation("ru.otus.otuskotlin.cryptotrade.libs:logging-logback")
}