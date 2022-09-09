package org.ddd.primitives.validation

import java.math.BigDecimal

fun notEmpty(value: String, description: String) = Validation(value, description, String::isNotEmpty)

fun notBlank(value: String, description: String) = Validation(value, description, String::isNotBlank)

fun minLength(value: String, description: String, minLength: Int): Validation<String> {
    return Validation(value, description) {
        it.length >= minLength
    }
}

fun maxLength(value: String, description: String, maxLength: Int): Validation<String> {
    return Validation(value, description) {
        it.length <= maxLength
    }
}

fun onlyContainNumbers(value: String, description: String) = mustConformRegEx(value, description, "[0-9]*".toRegex())

fun mustConformRegEx(value: String, description: String, regex: Regex): Validation<String> {
    return Validation(value, description) {
        it.matches(regex)
    }
}

fun greaterThanZero(value: BigDecimal, description: String): Validation<BigDecimal> {
    return Validation(value, description) {
        BigDecimal.ZERO < it
    }
}

fun notZero(value: BigDecimal, description: String): Validation<BigDecimal> {
    return Validation(value, description) {
        BigDecimal.ZERO != it
    }
}

fun <T> notEmpty(value: List<T>, description: String) = Validation(value, description, List<T>::isNotEmpty)
