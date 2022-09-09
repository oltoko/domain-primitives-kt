package org.ddd.primitives.validation

import java.math.BigDecimal

fun valid(): Validation = ValidValidation()

fun must(description: String, valid: () -> Boolean): Validation = SimpleValidation(description, valid)

fun <T> nullsafe(value: T?, description: String, valid: (T) -> Boolean): Validation {
    return if (value != null) {
        ValueValidation<T>(value, description, valid)
    } else {
        valid()
    }
}

fun notNull(value: Any?, description: String): Validation = ValueValidation(value, description) {
    it != null
}

fun notEmpty(value: String?, description: String): Validation = nullsafe(value, description, String::isNotEmpty)

fun notBlank(value: String?, description: String): Validation = nullsafe(value, description, String::isNotBlank)

fun minLength(value: String?, description: String, minLength: Int): Validation {
    return nullsafe(value, description) {
        it.length >= minLength
    }
}

fun maxLength(value: String?, description: String, maxLength: Int): Validation {
    return nullsafe(value, description) {
        it.length <= maxLength
    }
}

fun onlyContainNumbers(value: String?, description: String): Validation =
    conformRegEx(value, description, "[0-9]*".toRegex())

fun conformRegEx(value: String?, description: String, regex: Regex): Validation {
    return nullsafe(value, description) {
        it.matches(regex)
    }
}

fun greaterThanZero(value: BigDecimal?, description: String): Validation {
    return nullsafe(value, description) {
        BigDecimal.ZERO < it
    }
}

fun notZero(value: BigDecimal?, description: String): Validation {
    return nullsafe(value, description) {
        BigDecimal.ZERO != it
    }
}

fun <T> notEmpty(value: List<T>?, description: String): Validation =
    nullsafe(value, description, List<T>::isNotEmpty)
