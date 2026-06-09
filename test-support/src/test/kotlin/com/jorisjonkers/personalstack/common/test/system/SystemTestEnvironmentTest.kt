package com.jorisjonkers.personalstack.common.test.system

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SystemTestEnvironmentTest {
    @Test
    fun `reads configured properties with defaults`() {
        val environment =
            SystemTestEnvironment(
                mapOf(
                    "test.api.url" to "https://api.example.test",
                    "test.shard.index" to "2",
                    "test.shard.count" to "4",
                    "blank" to "",
                ),
            )

        assertThat(environment.apiUrl).isEqualTo("https://api.example.test")
        assertThat(environment.frontendUrl).isEqualTo("http://localhost:3000")
        assertThat(environment.shardIndex).isEqualTo(2)
        assertThat(environment.shardCount).isEqualTo(4)
        assertThat(environment.string("blank", "fallback")).isEqualTo("fallback")
    }
}
