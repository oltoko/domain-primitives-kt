package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.ddd.primitives.model.SingleValueObject
import org.ddd.primitives.validation.Validation
import org.ddd.primitives.validation.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class SingleValueObjectExampleTest {

    @Test
    internal fun `validates if all requirements are fulfilled`() {
        Name("Zaphod")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t", "\n"])
    internal fun `doesn't validate if blank`(invalidName: String) {

        val ex = shouldThrow<ValidationException> {
            Name(invalidName)
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "should not be blank"
        }
    }

    @Test
    internal fun `doesn't validate if less than 3 characters`() {

        val ex = shouldThrow<ValidationException> {
            Name("42")
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "should have min length 3"
        }
    }

    @Test
    internal fun `doesn't validate if more than 20 characters`() {

        val ex = shouldThrow<ValidationException> {
            Name("Great Green Arkleseizure")
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "should have max length 20"
        }
    }
}

internal class Name(name: String) : SingleValueObject<String>(
    name,
    Validation(name, "should not be blank") { it.isBlank().not() } and
            Validation(name, "should have min length 3") { it.length >= 3 } and
            Validation(name, "should have max length 20") { it.length <= 20 }
)