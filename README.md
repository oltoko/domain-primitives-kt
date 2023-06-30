# domain-primitives-kt

[![License](https://img.shields.io/hexpm/l/plug)](LICENSE) [![Build](https://github.com/oltoko/domain-primitives-kt/actions/workflows/verify-build.yml/badge.svg)](https://github.com/oltoko/domain-primitives-kt/actions/workflows/verify-build.yml)

Primitive, self-validating immutable object types for Kotlin.

# 🚀 Features

## ValueObject Type

The `SingleValueObject<T>` could be used to create a self-validating immutable object:

```kotlin
class Name(name: String) : SingleValueObject<String>(
    name,
    validation {
        notBlank(name, "must not be blank")
        minLength(name, "must have min length of 3", 3)
        maxLength(name, "must have max length of 20", 20)
    }
)
```

It's only possible to create a valid object if it matches the pattern. The `SingleValueObject` defines the value as
final, which results in an immutable object.

Creating a name object with an invalid value will throw an `ValidationViolationException`:

```kotlin
class SingleValueObjectExampleTest {

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
```

## Composed Value Objects, Aggregates and Entities

The abstract `ValueObject`, `Aggregate` and `Entity` classes allow expressive definitions with some helpful methods for
validation.

Have a look, e.g. at
the [`MonetaryAmount`](src/test/kotlin/org/ddd/primitives/examples/kotlin/ValueObjectExampleTest.kt) example.

# 📚 Releases

All available releases can be viewed in
the [release overview](https://github.com/oltoko/domain-primitives-kt/releases).

# 👩‍💻/👨‍💻 Contributing

Do you want to contribute to our open source project?
Read the [Contribution Guidelines](CONTRIBUTING.md) and get started 🙂

# 📨 Contact

If you have any questions or ideas feel free to create
an [issue](https://github.com/oltoko/domain-primitives-kt/issues).
