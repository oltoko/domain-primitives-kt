package org.ddd.primitives.validation

class ValidationException(
    label: String,
    message: String,
) : RuntimeException("$label is not valid: $message")