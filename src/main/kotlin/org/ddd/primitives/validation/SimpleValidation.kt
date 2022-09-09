package org.ddd.primitives.validation

class SimpleValidation(
    private val description: String,
    private val valid: () -> Boolean,
) : Validation {
    override fun validate() = if (valid()) {
        ValidationResult(true)
    } else {
        ValidationResult(false, description)
    }
}
