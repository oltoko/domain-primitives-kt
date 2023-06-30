package org.ddd.primitives.model

import org.ddd.primitives.validation.ValidationViolation

abstract class Entity<T>(
    violations: List<ValidationViolation>
) : DomainPrimitive(violations) {
    abstract fun businessKey(): T
}
