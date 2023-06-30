package org.ddd.primitives.model

import org.ddd.primitives.validation.ValidationViolation

sealed class DomainPrimitive(
    violations: List<ValidationViolation>
) {
    init {
        val validationViolations = violations.asSequence()
            .map { it.description }
            .joinToString("; ", limit = 20)

        if (validationViolations.isNotEmpty()) {
            throw ValidationViolationException(this.javaClass.simpleName, validationViolations)
        }
    }
}
