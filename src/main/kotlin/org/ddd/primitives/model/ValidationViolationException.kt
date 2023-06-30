package org.ddd.primitives.model

class ValidationViolationException(
    label: String,
    message: String,
) : RuntimeException("$label is not valid: $message")
