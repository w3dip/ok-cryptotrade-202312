package ru.otus.otuskotlin.crypto.trade.e2e.docker

import ru.otus.otuskotlin.crypto.trade.e2e.fixture.docker.AbstractDockerCompose

object KtorJvmKeycloakDockerCompose : AbstractDockerCompose(
    "envoy_1", 8080, "docker-compose-ktor-keycloak-jvm.yml"
)
//object KtorJvmKeycloakDockerCompose : DockerCompose {
//    override fun start() {
//    }
//
//    override fun stop() {
//    }
//
//    override fun clearDb() {
//
//    }
//
//    override val inputUrl: URLBuilder
//        get() = URLBuilder("http://localhost:8080/")
//}
