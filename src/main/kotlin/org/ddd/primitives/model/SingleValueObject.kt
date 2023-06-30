package org.ddd.primitives.model

import org.ddd.primitives.validation.ValidationViolation

abstract class SingleValueObject<T>(
    val value: T,
    violations: List<ValidationViolation>
) : DomainPrimitive(violations)
