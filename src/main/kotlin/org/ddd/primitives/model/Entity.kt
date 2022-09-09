package org.ddd.primitives.model

import org.ddd.primitives.validation.Validation

abstract class Entity<T>(vararg validations: Validation<*>) : DomainPrimitive(validations.asList()) {
    abstract fun businessKey(): T
}
