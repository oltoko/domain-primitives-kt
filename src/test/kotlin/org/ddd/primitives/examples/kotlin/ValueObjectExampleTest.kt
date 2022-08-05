package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldThrow
import org.ddd.primitives.model.ValueObject
import org.ddd.primitives.validation.Validation.Companion.check
import org.ddd.primitives.validation.ValidationException
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Currency

internal class ValueObjectExampleTest {

    @Test
    internal fun `validate if all requirements are fulfilled`() {
        MonetaryAmount(BigDecimal("12.12"), EUR)
    }

    @Test
    internal fun `doesn't validate if fraction digits are wrong`() {
        shouldThrow<ValidationException> {
            MonetaryAmount(BigDecimal("12.123"), CHF)
        }
    }
}

internal data class MonetaryAmount(
    val amount: BigDecimal,
    val currency: Currency,
) : ValueObject(
    check(
        amount,
        "amount should match fraction digits of ${currency.defaultFractionDigits}"
    ) { it.scale() <= currency.defaultFractionDigits } and
            check(
                amount,
                "amount should not be Zero"
            ) { it != BigDecimal.ZERO },
    check(currency, "€ and $ only") { supportedCurrencies.contains(currency) }) {

}

val EUR = Currency.getInstance("EUR")
val USD = Currency.getInstance("USD")
val CHF = Currency.getInstance("CHF")
val JPY = Currency.getInstance("JPY")

val supportedCurrencies = listOf(EUR, USD, CHF)
