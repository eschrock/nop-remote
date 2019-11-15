package io.titandata.remote.nop.server

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.mockk.clearAllMocks
import io.titandata.remote.RemoteOperation
import io.titandata.remote.RemoteOperationType

class NopRemoteServerTest : StringSpec() {
    private val client = NopRemoteServer()

    private val op = RemoteOperation(
            operationId = "op",
            commitId = "commit",
            remote = emptyMap(),
            parameters = emptyMap(),
            type = RemoteOperationType.PUSH,
            data = null,
            updateProgress = { _, _, _ -> }
    )

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {

        "validate empy remote succeeds" {
            val result = client.validateRemote(emptyMap())
            result.size shouldBe 0
        }

        "validate remote with properties fails" {
            shouldThrow<IllegalArgumentException> {
                client.validateRemote(mapOf("a" to "b"))
            }
        }

        "validate empty parameters succeeds" {
            val result = client.validateParameters(emptyMap())
            result.size shouldBe 0
        }

        "validate delay converts to integer" {
            val result = client.validateParameters((mapOf("delay" to 12.0)))
            result["delay"] shouldBe 12
        }

        "validate unknown properties fails" {
            shouldThrow<IllegalArgumentException> {
                client.validateParameters(mapOf("a" to "b"))
            }
        }

        "get provider returns nop" {
            client.getProvider() shouldBe "nop"
        }

        "get commit returns an empty commit" {
            val commit = client.getCommit(emptyMap(), emptyMap(), "id")
            commit shouldNotBe null
            commit!!.size shouldBe 0
        }

        "list commits returns an empty list" {
            val commits = client.listCommits(emptyMap(), emptyMap(), emptyList())
            commits.size shouldBe 0
        }

        "end operation does nothing" {
            client.endOperation(op, true)
        }

        "sync volume does nothing" {
            client.syncVolume(op, "volume", "volume", "/path", "/path")
        }

        "start operation succeeds" {
            client.startOperation(op)
        }
    }
}
