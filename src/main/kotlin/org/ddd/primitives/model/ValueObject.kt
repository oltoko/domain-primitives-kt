package org.ddd.primitives.model

import org.ddd.primitives.validation.Validation

abstract class ValueObject(vararg validations: Validation) : DomainPrimitive(validations.asList())
