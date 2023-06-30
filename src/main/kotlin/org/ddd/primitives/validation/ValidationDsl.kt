package org.ddd.primitives.validation

import java.math.BigDecimal

class ValidationDsl(
    val results: MutableList<ValidationViolation> = mutableListOf()
) {

    fun check(description: String, valid: () -> Boolean) {
        if (valid().not()) {
            results.add(ValidationViolation(description))
        }
    }

    fun <T> check(value: T, description: String, valid: (T) -> Boolean) {
        if (valid(value).not()) {
            results.add(ValidationViolation(description))
        }
    }

    fun <T> checkIgnoreNull(value: T?, description: String, valid: (T) -> Boolean) {
        if (value != null) {
            check(value, description, valid)
        }
    }

    fun notNull(value: Any?, description: String) = check(value, description) {
        it != null
    }

    fun notEmpty(value: String?, description: String) =
        checkIgnoreNull(value, description, String::isNotEmpty)

    fun notBlank(value: String?, description: String) =
        checkIgnoreNull(value, description, String::isNotBlank)

    fun minLength(value: String?, description: String, minLength: Int) = checkIgnoreNull(value, description) {
        it.length >= minLength
    }

    fun maxLength(value: String?, description: String, maxLength: Int) = checkIgnoreNull(value, description) {
        it.length <= maxLength
    }

    fun onlyContainNumbers(value: String?, description: String) = conformRegEx(value, description, "[0-9]*".toRegex())

    fun conformRegEx(value: String?, description: String, regex: Regex) = checkIgnoreNull(value, description) {
        it.matches(regex)
    }

    fun greaterThanZero(value: BigDecimal?, description: String) = checkIgnoreNull(value, description) {
        BigDecimal.ZERO < it
    }

    fun notZero(value: BigDecimal?, description: String) = checkIgnoreNull(value, description) {
        BigDecimal.ZERO != it
    }

    fun <T> notEmpty(value: List<T>?, description: String) = checkIgnoreNull(value, description, List<T>::isNotEmpty)
}