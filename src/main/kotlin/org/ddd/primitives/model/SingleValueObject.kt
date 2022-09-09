package org.ddd.primitives.model

import org.ddd.primitives.validation.Validation

abstract class SingleValueObject<T>(
    val value: T,
    vararg validations: Validation<T>
) : DomainPrimitive(validations.asList())
