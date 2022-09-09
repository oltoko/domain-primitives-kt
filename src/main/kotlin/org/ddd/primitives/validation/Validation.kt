package org.ddd.primitives.validation

class Validation<T>(
    private val value: T,
    private val description: String,
    private val valid: (T) -> Boolean
) {
    fun validate() = if (valid(value)) {
        ValidationResult(true)
    } else {
        ValidationResult(false, description)
    }
}

data class ValidationResult(
    val valid: Boolean,
    val error: String? = null
)
