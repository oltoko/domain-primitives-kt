package org.ddd.primitives.model

import org.ddd.primitives.validation.Validatable
import org.ddd.primitives.validation.Validation
import org.ddd.primitives.validation.ValidationException

abstract class DomainPrimitive(
    private val validations: List<Validation<*>>
) : Validatable {

    init {
        this.validate()
    }

    override fun validate() {

        val validationErrors = validations
            .map { it.validate() }
            .filter { it.valid.not() }
            .map { it.error }
            .joinToString("; ")

        if (validationErrors.isNotBlank()) {
            throw ValidationException(this.javaClass.simpleName, validationErrors)
        }
    }
}
