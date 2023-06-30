package org.ddd.primitives.model

import org.ddd.primitives.validation.ValidationViolation

abstract class ValueObject(
    violations: List<ValidationViolation>
) : DomainPrimitive(violations)
