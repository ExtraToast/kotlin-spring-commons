package com.jorisjonkers.personalstack.common.test.system

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class TotpTestCodesTest {
    @Test
    fun `generates RFC 6238 test vector code`() {
        val secret = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ"

        val code = TotpTestCodes.generate(secret, timeMillis = 59_000, digits = 8)

        assertThat(code).isEqualTo("94287082")
    }

    @Test
    fun `decodes base32 secrets without padding`() {
        assertThat(TotpTestCodes.decodeBase32("MY").decodeToString()).isEqualTo("f")
    }

    @Test
    fun `rejects invalid base32 characters`() {
        assertThatThrownBy { TotpTestCodes.decodeBase32("not-valid!") }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Invalid Base32")
    }
}
