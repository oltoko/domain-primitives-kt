package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.ddd.primitives.model.*
import org.ddd.primitives.validation.noValidation
import org.ddd.primitives.validation.validation
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class AggregateExampleTest {

    @Test
    internal fun `should accept valid order`() {
        shouldNotThrow<Exception> {
            Order(
                customer = Customer(
                    customerNumber = "123456",
                    userName = "asdf",
                    emailAddress = "asdf@example.com",
                ),
                items = listOf(
                    Item(
                        articleNo = ArticleNumber("456"),
                        price = Price(BigDecimal("1.99"), Currency.getInstance("EUR"))
                    ),
                    Item(
                        articleNo = ArticleNumber("798"),
                        price = Price(BigDecimal("9.99"), Currency.getInstance("EUR"))
                    ),
                ),
                billingAddress = Address(
                    name = "Mr. Test",
                    street = "Test Street 45",
                    zipCode = "12345",
                    city = "Test City",
                    countryCode = "DE"
                )
            )
        }
    }

    @Test
    internal fun `order should throw if item list is empty`() {
        val exc = shouldThrow<ValidationViolationException> {
            Order(
                customer = Customer(
                    customerNumber = "123456",
                    userName = "asdf",
                    emailAddress = "asdf@example.com",
                ),
                items = listOf(),
                billingAddress = Address(
                    name = "Mr. Test",
                    street = "Test Street 45",
                    zipCode = "12345",
                    city = "Test City",
                    countryCode = "DE"
                )
            )
        }

        exc.message shouldContain "Item List must not be empty"
    }

    @Test
    internal fun `customer should throw if customer number is blank`() {
        val exc = shouldThrow<ValidationViolationException> {
            Customer(
                customerNumber = "   ",
                userName = "asdf",
                emailAddress = "asdf@example.com",
            )
        }

        exc.message shouldContain "Customer number must not be blank"
    }

    @Test
    internal fun `customer should throw if email is not valid`() {
        val exc = shouldThrow<ValidationViolationException> {
            Customer(
                customerNumber = "123456",
                userName = "asdf",
                emailAddress = "asdf@test@example.com",
            )
        }

        exc.message shouldContain "email must be valid"
    }

    @Test
    internal fun `customer should throw if invalid after copy`() {

        val customer = Customer(
            customerNumber = "123456",
            userName = "asdf",
            emailAddress = "asdf@example.com",
        )

        val exc = shouldThrow<ValidationViolationException> {
            customer.copy(customerNumber = "    ")
        }

        exc.message shouldContain "Customer number must not be blank"
    }
}

internal data class Order(
    val customer: Customer,
    val items: List<Item>,
    val billingAddress: Address,
    val shippingAddress: Address? = null
) : Aggregate(
    validation {
        notEmpty(items, "Item List must not be empty")
        check("Billing and shipping address must not be equal") {
            billingAddress != shippingAddress
        }
    }
)

internal data class Customer(
    val customerNumber: String,
    val userName: String,
    val emailAddress: String,
    val phoneNumber: String? = null
) : Entity<String>(
    validation {
        notBlank(customerNumber, "Customer number must not be blank")
        onlyContainNumbers(customerNumber, "Customer number must only consist of numbers")
        notBlank(userName, "User name must not be blank")
        conformRegEx(emailAddress, "email must be valid", "^[^@]+@[^@]+\\.[^@]+\$".toRegex())
        maxLength(phoneNumber, "phone number must have max length of 30", 30)
    }
) {
    override fun businessKey() = customerNumber
}

internal data class Item(
    val articleNo: ArticleNumber,
    val price: Price,
) : Entity<ArticleNumber>(noValidation()) {
    override fun businessKey() = articleNo
}

internal data class ArticleNumber(
    val number: String,
) : SingleValueObject<String>(
    number,
    validation {
        notBlank(number, "Article number must not be blank")
        onlyContainNumbers(number, "Article number must only consists of numbers")
    }
)

internal data class Price(
    val price: BigDecimal,
    val currency: Currency,
) : ValueObject(
    validation {
        greaterThanZero(price, "Price must be greater than Zero")
    }
)

internal data class Address(
    val name: String,
    val street: String,
    val zipCode: String,
    val city: String,
    val countryCode: String?,
) : ValueObject(
    validation {
        notBlank(name, "Name must not be blank")
        notBlank(street, "Street must not be blank")
        notBlank(zipCode, "ZipCode must not be blank")
        notBlank(city, "City must not be blank")
        checkIgnoreNull(countryCode, "Country Code needs to be an existing one") {
            it.let { isoCountryCodes.contains(it) }
        }
    }
)

val isoCountryCodes: Set<String> = Locale.getISOCountries().asSequence().toSet()
