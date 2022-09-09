package org.ddd.primitives.examples.java;

import org.ddd.primitives.model.ValueObject;
import org.ddd.primitives.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.ddd.primitives.validation.ValidationsKt.*;

public class ValueObjectExampleTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency CHF = Currency.getInstance("CHF");
    private static final Currency JPY = Currency.getInstance("JPY");

    @Test
    void validate_if_all_requirements_are_fulfilled() {
        assertThatCode(() -> new MonetaryAmount(new BigDecimal("12.12"), EUR))
            .doesNotThrowAnyException();
    }

    @Test
    void doesnt_validate_if_fraction_digits_are_wrong() {
        assertThatThrownBy(() -> new MonetaryAmount(new BigDecimal("12.123"), CHF))
            .isInstanceOf(ValidationException.class)
            .hasMessageContainingAll("MonetaryAmount is not valid", "amount must match fraction digits of 2");
    }

    @Test
    void doesnt_validate_if_currency_is_wrong() {
        assertThatThrownBy(() -> new MonetaryAmount(new BigDecimal("12.12"), JPY))
            .isInstanceOf(ValidationException.class)
            .hasMessageContainingAll("MonetaryAmount is not valid", "€, $ and CHF only");
    }

    @Test
    void doesnt_validate_if_amount_is_null() {
        assertThatCode(() -> new MonetaryAmount(null, EUR))
            .doesNotThrowAnyException();
    }

    private static class MonetaryAmount extends ValueObject {

        private static final Set<Currency> SUPPORTED_CURRENCIES = Set.of(EUR, USD, CHF);

        private final BigDecimal amount;

        private final Currency currency;

        private MonetaryAmount(BigDecimal amount, Currency currency) {
            super(
                notNull(currency, "currency must not be null"),
                notZero(amount, "amount must not be Zero"),
                nullsafe(amount, "amount must match fraction digits of " + currency.getDefaultFractionDigits(), a -> a.scale() <= currency.getDefaultFractionDigits()),
                nullsafe(currency, "€, $ and CHF only", SUPPORTED_CURRENCIES::contains)
            );
            this.amount = amount;
            this.currency = currency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MonetaryAmount that = (MonetaryAmount) o;
            return amount.equals(that.amount) && currency.equals(that.currency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(amount, currency);
        }
    }
}
