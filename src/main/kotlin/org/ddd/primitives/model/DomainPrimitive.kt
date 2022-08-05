package org.ddd.primitives.model

import org.ddd.primitives.validation.Validation

abstract class DomainPrimitive(
    private val validations: List<Validation<*>>
) : Validatable {

    init {
        this.validate()
    }

    override fun validate() {
        validations.forEach { it.validate(this.javaClass.simpleName) }
    }
}

abstract class SingleValueObject<T>(
    val value: T,
    vararg validations: Validation<T>
) : DomainPrimitive(validations.asList())

abstract class ValueObject(vararg validations: Validation<*>) : DomainPrimitive(validations.asList())

abstract class Entity(vararg validations: Validation<*>) : DomainPrimitive(validations.asList())

abstract class Aggregate(vararg validations: Validation<*>) : DomainPrimitive(validations.asList())
