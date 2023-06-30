package org.ddd.primitives.model

import org.ddd.primitives.validation.ValidationViolation

abstract class Aggregate(
    violations: List<ValidationViolation>
) : DomainPrimitive(violations)
