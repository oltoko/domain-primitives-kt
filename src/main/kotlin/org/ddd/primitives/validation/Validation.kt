package org.ddd.primitives.validation

fun noValidation() = listOf<ValidationViolation>()

fun validation(init: ValidationDsl.() -> Unit): List<ValidationViolation> {
    val dsl = ValidationDsl()
    dsl.init()
    return dsl.results
}

