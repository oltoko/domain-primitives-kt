package org.ddd.primitives.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class ValidationDslTest {

    @Test
    internal fun `notEmpty should validate on not empty string`() {
        val actual = validation {
            notEmpty("test", "test must not be empty")
        }
        actual.shouldBeEmpty()
    }

    @Test
    internal fun `notEmpty should not validate on empty string`() {
        val actual = validation {
            notEmpty("", "test must not be empty")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("test must not be empty")
    }

    @Test
    internal fun `notBlank should validated on not blank string`() {
        val actual = validation {
            notBlank("test", "test must not be blank")
        }
        actual.shouldBeEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "    ", "\t", "\n"])
    internal fun `notBlank should not validate on blank string`(value: String) {
        val actual = validation {
            notBlank(value, "test must not be blank")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("test must not be blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["   ", "ab ", "abc", "abcd", "The quick brown fox jumps over the lazy dog"])
    internal fun `minLength should validate on long enough string`(value: String) {
        val actual = validation {
            minLength(value, "value must have min length 3", 3)
        }
        actual.shouldBeEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "ab"])
    internal fun `minLength should not validate on not long enough string`(value: String) {
        val actual = validation {
            minLength(value, "value must have min length 3", 3)
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("value must have min length 3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "ab"])
    internal fun `maxLength should validate on short enough string`(value: String) {
        val actual = validation {
            maxLength(value, "value must have max length 3", 3)
        }
        actual.shouldBeEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["    ", "abc ", "abcd", "The quick brown fox jumps over the lazy dog"])
    internal fun `maxLength should not validate on not short enough string`(value: String) {
        val actual = validation {
            maxLength(value, "value must have max length 3", 3)
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("value must have max length 3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "12345", "098745618948111901890"])
    internal fun `onlyContainNumbers should validate if string consist only of numbers`(value: String) {
        val actual = validation {
            onlyContainNumbers(value, "value must only contain of numbers")
        }
        actual.shouldBeEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = [" 1", "123a45", "asdf", "🫣"])
    internal fun `onlyContainNumbers should not validate if string consist not only of numbers`(value: String) {
        val actual = validation {
            onlyContainNumbers(value, "value must only contain of numbers")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("value must only contain of numbers")
    }

    @ParameterizedTest
    @ValueSource(strings = ["0.000001", "1", "4891981456.4564"])
    internal fun `greaterThanZero should validate if number is greater than zero`(value: String) {
        val actual = validation {
            greaterThanZero(BigDecimal(value), "value should be greater than zero")
        }
        actual.shouldBeEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "-0.0000001", "-4891981456.4564"])
    internal fun `greaterThanZero should not validate if number is zero or below`(value: String) {
        val actual = validation {
            greaterThanZero(BigDecimal(value), "value should be greater than zero")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("value should be greater than zero")
    }

    @ParameterizedTest
    @ValueSource(strings = ["0.000001", "1", "4891981456.4564", "-0.000001", "-1", "-4891981456.4564"])
    internal fun `notZero should validated if number is not zero`(value: String) {
        val actual = validation {
            notZero(BigDecimal(value), "value should not be zero")
        }
        actual.shouldBeEmpty()
    }

    @Test
    internal fun `notZero should not validated if number is zero`() {
        val actual = validation {
            notZero(BigDecimal.ZERO, "value should not be zero")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("value should not be zero")
    }

    @Test
    internal fun `notEmpty should validate if list is not empty`() {
        val actual = validation {
            notEmpty(listOf("test"), "list should not be empty")
        }
        actual.shouldBeEmpty()
    }

    @Test
    internal fun `notEmpty should not validate if list is empty`() {
        val actual = validation {
            notEmpty(listOf<String>(), "list should not be empty")
        }
        actual.shouldNotBeEmpty()
        actual shouldContain ValidationViolation("list should not be empty")
    }
}
