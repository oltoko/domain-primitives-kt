package org.ddd.primitives.validation

interface Validation {
    fun validate(): ValidationResult
}

data class ValidationResult(
    val valid: Boolean,
    val error: String? = null
)
