package ru.otus.otuskotlin.crypto.trade.e2e.docker

import ru.otus.otuskotlin.crypto.trade.e2e.fixture.docker.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock_1", 8080, "docker-compose-wiremock.yml"
)
