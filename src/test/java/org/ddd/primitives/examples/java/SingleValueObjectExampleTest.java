package org.ddd.primitives.examples.java;

import org.ddd.primitives.model.SingleValueObject;
import org.ddd.primitives.model.ValidationViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.ddd.primitives.validation.ValidationKt.validation;

class SingleValueObjectExampleTest {

    @Test
    void validates_if_all_requirements_are_fulfilled() {
        assertThatCode(() -> new Name("Zaphod"))
                .doesNotThrowAnyException();
    }

    @Test
    void doesnt_validate_if_value_is_null() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(ValidationViolationException.class)
                .hasMessageContainingAll("Name is not valid", "must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "    ", "\t", "\n"})
    void doesnt_validated_if_blank(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(ValidationViolationException.class)
                .hasMessageContainingAll("Name is not valid", "must not be blank");
    }

    @Test
    void doesnt_validate_if_less_than_3_characters() {
        assertThatThrownBy(() -> new Name("42"))
                .isInstanceOf(ValidationViolationException.class)
                .hasMessageContainingAll("Name is not valid", "must have min length of 3");
    }

    @Test
    void doesnt_validate_if_more_than_20_characters() {
        assertThatThrownBy(() -> new Name("Great Green Arkleseizure"))
                .isInstanceOf(ValidationViolationException.class)
                .hasMessageContainingAll("Name is not valid", "must have max length of 20");
    }

    private static class Name extends SingleValueObject<String> {
        public Name(String name) {
            super(name,
                    validation(dsl -> {
                        dsl.notNull(name, "must not be null");
                        dsl.notBlank(name, "must not be blank");
                        dsl.minLength(name, "must have min length of 3", 3);
                        dsl.maxLength(name, "must have max length of 20", 20);
                        return null;
                    })
            );
        }
    }
}
