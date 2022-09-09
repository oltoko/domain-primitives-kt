package org.ddd.primitives.validation

import java.math.BigDecimal

fun must(description: String, valid: () -> Boolean): Validation = SimpleValidation(description, valid)

fun notEmpty(value: String, description: String): Validation = ValueValidation(value, description, String::isNotEmpty)

fun notBlank(value: String, description: String): Validation = ValueValidation(value, description, String::isNotBlank)

fun minLength(value: String, description: String, minLength: Int): Validation {
    return ValueValidation(value, description) {
        it.length >= minLength
    }
}

fun maxLength(value: String, description: String, maxLength: Int): Validation {
    return ValueValidation(value, description) {
        it.length <= maxLength
    }
}

fun onlyContainNumbers(value: String, description: String): Validation =
    conformRegEx(value, description, "[0-9]*".toRegex())

fun conformRegEx(value: String, description: String, regex: Regex): Validation {
    return ValueValidation(value, description) {
        it.matches(regex)
    }
}

fun greaterThanZero(value: BigDecimal, description: String): Validation {
    return ValueValidation(value, description) {
        BigDecimal.ZERO < it
    }
}

fun notZero(value: BigDecimal, description: String): Validation {
    return ValueValidation(value, description) {
        BigDecimal.ZERO != it
    }
}

fun <T> notEmpty(value: List<T>, description: String): Validation =
    ValueValidation(value, description, List<T>::isNotEmpty)
