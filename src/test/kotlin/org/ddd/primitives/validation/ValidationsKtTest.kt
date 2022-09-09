package org.ddd.primitives.validation

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class ValidationsKtTest {

    @Test
    internal fun `notEmpty should validate on not empty string`() {
        val actual = notEmpty("test", "test must not be empty").validate()
        assertValid(actual)
    }

    @Test
    internal fun `notEmpty should not validate on empty string`() {
        val actual = notEmpty("", "test must not be empty").validate()
        assertInvalid(actual, "test must not be empty")
    }

    @Test
    internal fun `notBlank should validated on not blank string`() {
        val actual = notBlank("test", "test must not be blank").validate()
        assertValid(actual)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t", "\n"])
    internal fun `notBlank should not validate on blank string`(value: String) {
        val actual = notBlank(value, "test must not be blank").validate()
        assertInvalid(actual, "test must not be blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["   ", "ab ", "abc", "abcd", "The quick brown fox jumps over the lazy dog"])
    internal fun `minLength should validate on long enough string`(value: String) {
        val actual = minLength(value, "value must have min length 3", 3).validate()
        assertValid(actual)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "ab"])
    internal fun `minLength should not validate on not long enough string`(value: String) {
        val actual = minLength(value, "value must have min length 3", 3).validate()
        assertInvalid(actual, "value must have min length 3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "ab"])
    internal fun `maxLength should validate on short enough string`(value: String) {
        val actual = maxLength(value, "value must have max length 3", 3).validate()
        assertValid(actual)
    }

    @ParameterizedTest
    @ValueSource(strings = ["    ", "abc ", "abcd", "The quick brown fox jumps over the lazy dog"])
    internal fun `maxLength should not validate on not short enough string`(value: String) {
        val actual = maxLength(value, "value must have max length 3", 3).validate()
        assertInvalid(actual, "value must have max length 3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "12345", "098745618948111901890"])
    internal fun `onlyContainNumbers should validate if string consist only of numbers`(value: String) {
        val actual = onlyContainNumbers(value, "value must only contain of numbers").validate()
        assertValid(actual)
    }

    @ParameterizedTest
    @ValueSource(strings = [" 1", "123a45", "asdf", "🫣"])
    internal fun `onlyContainNumbers should not validate if string consist not only of numbers`(value: String) {
        val actual = onlyContainNumbers(value, "value must only contain of numbers").validate()
        assertInvalid(actual, "value must only contain of numbers")
    }

    @ParameterizedTest
    @ValueSource(strings = ["0.000001", "1", "4891981456.4564"])
    internal fun `greaterThanZero should validate if number is greater than zero`(value: String) {
        val actual = greaterThanZero(BigDecimal(value), "value should be greater than zero").validate()
        assertValid(actual)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "-0.0000001", "-4891981456.4564"])
    internal fun `greaterThanZero should not validate if number is zero or below`(value: String) {
        val actual = greaterThanZero(BigDecimal(value), "value should be greater than zero").validate()
        assertInvalid(actual, "value should be greater than zero")
    }

    @ParameterizedTest
    @ValueSource(strings = ["0.000001", "1", "4891981456.4564", "-0.000001", "-1", "-4891981456.4564"])
    internal fun `notZero should validated if number is not zero`(value: String) {
        val actual = notZero(BigDecimal(value), "value should not be zero").validate()
        assertValid(actual)
    }

    @Test
    internal fun `notZero should not validated if number is zero`() {
        val actual = notZero(BigDecimal.ZERO, "value should not be zero").validate()
        assertInvalid(actual, "value should not be zero")
    }

    @Test
    internal fun `notEmpty should validate if list is not empty`() {
        val actual = notEmpty(listOf("test"), "list should not be empty").validate()
        assertValid(actual)
    }

    @Test
    internal fun `notEmpty should not validate if list is empty`() {
        val actual = notEmpty(listOf<String>(), "list should not be empty").validate()
        assertInvalid(actual, "list should not be empty")
    }

    private fun assertValid(result: ValidationResult) {
        result.valid shouldBe true
        result.error shouldBe null
    }

    private fun assertInvalid(result: ValidationResult, errorText: String) {
        result.valid shouldBe false
        result.error shouldBe errorText
    }
}
