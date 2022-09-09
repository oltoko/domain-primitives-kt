package org.ddd.primitives.validation

class ValueValidation<T>(
    private val value: T,
    private val description: String,
    private val valid: (T) -> Boolean
) : Validation {
    override fun validate() = if (valid(value)) {
        ValidationResult(true)
    } else {
        ValidationResult(false, description)
    }
}
