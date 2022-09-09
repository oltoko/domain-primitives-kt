package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import org.ddd.primitives.model.ValueObject
import org.ddd.primitives.validation.ValidationException
import org.ddd.primitives.validation.ValueValidation
import org.ddd.primitives.validation.notZero
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Currency

internal class ValueObjectExampleTest {

    @Test
    internal fun `validate if all requirements are fulfilled`() {
        shouldNotThrow<ValidationException> {
            MonetaryAmount(BigDecimal("12.12"), EUR)
        }
    }

    @Test
    internal fun `doesn't validate if fraction digits are wrong`() {
        shouldThrow<ValidationException> {
            MonetaryAmount(BigDecimal("12.123"), CHF)
        }
    }

    @Test
    internal fun `doesn't validate if currency is wrong`() {
        shouldThrow<ValidationException> {
            MonetaryAmount(BigDecimal("12"), JPY)
        }
    }
}

internal data class MonetaryAmount(
    val amount: BigDecimal,
    val currency: Currency,
) : ValueObject(
    ValueValidation(
        amount,
        "amount should match fraction digits of ${currency.defaultFractionDigits}"
    ) { it.scale() <= currency.defaultFractionDigits },
    notZero(amount, "amount should not be Zero"),
    ValueValidation(currency, "€, $ and CHF only") { supportedCurrencies.contains(currency) }
)

val EUR: Currency = Currency.getInstance("EUR")
val USD: Currency = Currency.getInstance("USD")
val CHF: Currency = Currency.getInstance("CHF")
val JPY: Currency = Currency.getInstance("JPY")

val supportedCurrencies = listOf(EUR, USD, CHF)
