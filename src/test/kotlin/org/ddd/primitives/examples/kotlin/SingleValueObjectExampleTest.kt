package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.ddd.primitives.model.SingleValueObject
import org.ddd.primitives.model.ValidationViolationException
import org.ddd.primitives.validation.validation
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class SingleValueObjectExampleTest {

    @Test
    internal fun `validates if all requirements are fulfilled`() {
        shouldNotThrow<ValidationViolationException> {
            Name("Zaphod")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "    ", "\t", "\n"])
    internal fun `doesn't validate if blank`(invalidName: String) {

        val ex = shouldThrow<ValidationViolationException> {
            Name(invalidName)
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "must not be blank"
        }
    }

    @Test
    internal fun `doesn't validate if less than 3 characters`() {

        val ex = shouldThrow<ValidationViolationException> {
            Name("42")
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "must have min length of 3"
        }
    }

    @Test
    internal fun `doesn't validate if more than 20 characters`() {

        val ex = shouldThrow<ValidationViolationException> {
            Name("Great Green Arkleseizure")
        }

        with(ex.message) {
            this shouldContain "Name is not valid"
            this shouldContain "must have max length of 20"
        }
    }
}

internal class Name(name: String) : SingleValueObject<String>(
    name,
    validation {
        notBlank(name, "must not be blank")
        minLength(name, "must have min length of 3", 3)
        maxLength(name, "must have max length of 20", 20)
    }
)
