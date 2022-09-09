package org.ddd.primitives.model

import org.ddd.primitives.validation.Validation

abstract class Aggregate(vararg validations: Validation<*>) : DomainPrimitive(validations.asList())
