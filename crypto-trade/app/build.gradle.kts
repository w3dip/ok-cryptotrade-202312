plugins {
    id("build-jvm")
    alias(libs.plugins.ktor)
    //alias(libs.plugins.muschko.remote)
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

    implementation(libs.logback)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":core"))
    implementation(project(":mappers"))


    /*implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.yaml)

    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.server.headers.default)

    implementation(libs.ktor.serialization.jackson)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)*/


//    implementation(project(":ok-marketplace-app-common"))
//    implementation(project(":ok-marketplace-biz"))

    // Stubs
//    implementation(project(":ok-marketplace-stubs"))

    //implementation(kotlin("stdlib-jdk8"))

    // transport models


    //testImplementation(kotlin("test-junit"))
    //testImplementation(kotlin("test"))
    //testImplementation(kotlin("test-common"))
    //testImplementation(kotlin("test-annotations-common"))

    //testImplementation(libs.ktor.server.test)
    //testImplementation(libs.ktor.client.negotiation)
}

//TODO сделать сборку и выгрузку образа