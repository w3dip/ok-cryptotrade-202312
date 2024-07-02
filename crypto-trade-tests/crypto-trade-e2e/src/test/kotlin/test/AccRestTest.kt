package ru.otus.otuskotlin.crypto.trade.e2e.test

import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.crypto.trade.e2e.docker.KtorJvmKeycloakDockerCompose
import ru.otus.otuskotlin.crypto.trade.e2e.docker.WiremockDockerCompose
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.BaseFunSpec
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.RestAuthClient
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.RestClient
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.docker.DockerCompose
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1.toV1

enum class TestDebug {
    STUB, PROD, TEST
}

// Kotest не сможет подставить правильный аргумент конструктора, поэтому
// нужно запретить ему запускать этот класс
@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
})

class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)

class AccRestKtorKeycloakJvmTest : BaseFunSpec(KtorJvmKeycloakDockerCompose, {
    val restClient = RestAuthClient(KtorJvmKeycloakDockerCompose)
    val debug = TestDebug.TEST
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})

