package com.jorisjonkers.personalstack.common.test.system

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class FqcnShardConditionTest {
    @Test
    fun `assigns each fqcn to exactly one shard`() {
        val fqcn = "com.example.systemtests.AccountFlowTest"
        val owners = (1..5).filter { FqcnShardCondition.owns(fqcn, it, 5) }

        assertThat(owners).hasSize(1)
    }

    @Test
    fun `rejects invalid shard configuration`() {
        assertThatThrownBy { FqcnShardCondition.owns("Test", 0, 2) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { FqcnShardCondition.owns("Test", 1, 0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
