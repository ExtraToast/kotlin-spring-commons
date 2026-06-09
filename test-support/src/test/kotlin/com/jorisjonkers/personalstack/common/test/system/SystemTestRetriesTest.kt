package com.jorisjonkers.personalstack.common.test.system

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.net.ConnectException
import java.util.concurrent.atomic.AtomicInteger

class SystemTestRetriesTest {
    @Test
    fun `retries connection failures and returns eventual result`() {
        val attempts = AtomicInteger()

        val result =
            SystemTestRetries.retryOnConnectionFailure(attempts = 3, delayMillis = 0) {
                if (attempts.incrementAndGet() < 3) {
                    throw RuntimeException(ConnectException("refused"))
                }
                "ok"
            }

        assertThat(result).isEqualTo("ok")
        assertThat(attempts.get()).isEqualTo(3)
    }

    @Test
    fun `does not retry unrelated failures`() {
        val attempts = AtomicInteger()

        assertThatThrownBy {
            SystemTestRetries.retryOnConnectionFailure(attempts = 3, delayMillis = 0) {
                attempts.incrementAndGet()
                error("bad request")
            }
        }.isInstanceOf(IllegalStateException::class.java)
        assertThat(attempts.get()).isEqualTo(1)
    }
}
