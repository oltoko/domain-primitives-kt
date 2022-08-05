package org.ddd.primitives.validation

class Validation<T>(
    private val value: T,
    private val description: String,
    private val valid: (T) -> Boolean
) {

    companion object {
        fun <T> check(value: T, description: String, validation: (T) -> Boolean): Validation<T> {
            return Validation(value, description, validation)
        }
    }

    private var additional: Validation<T>? = null

    fun validate(label: String) {
        doValidate()?.let { throw ValidationException(label, it) }
    }

    private fun doValidate(): String? {

        val additionalIssue = additional?.doValidate()

        return if (valid(value)) {
            additionalIssue
        } else {
            if (additionalIssue != null) {
                "$description and $additionalIssue"
            } else {
                description
            }
        }
    }

    infix fun and(additional: Validation<T>): Validation<T> {
        additional.additional = this
        return additional
    }
}
