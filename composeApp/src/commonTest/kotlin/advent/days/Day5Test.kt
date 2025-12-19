package advent.days

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class Day5Test {
    @Test
    fun areSeparatedTest() {
        val tests = listOf(
            RangeTestCase(1L..3L, 2L..4L, false),
            RangeTestCase(1L..3L, 5L..7L, true),
            RangeTestCase(2L..5L, 1L..2L, false, expectError = true),
            RangeTestCase(2L..0L, 5L..0L, false, expectError = true),

        )
        for (test in tests) {
            if (test.expectError) {
                shouldThrow<IllegalStateException> {
                    areSeparated(test.range1, test.range2)
                }
            } else {
                val result = areSeparated(test.range1, test.range2)
                result shouldBe test.separated
            }
        }
    }
}

private data class RangeTestCase(
    val range1: LongRange,
    val range2: LongRange,
    val separated: Boolean,
    val expectError: Boolean = false
)
