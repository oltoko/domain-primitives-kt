package org.ddd.primitives.examples.kotlin

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.ddd.primitives.model.Aggregate
import org.ddd.primitives.model.Entity
import org.ddd.primitives.model.SingleValueObject
import org.ddd.primitives.model.ValueObject
import org.ddd.primitives.validation.ValidationException
import org.ddd.primitives.validation.ValueValidation
import org.ddd.primitives.validation.conformRegEx
import org.ddd.primitives.validation.greaterThanZero
import org.ddd.primitives.validation.must
import org.ddd.primitives.validation.notBlank
import org.ddd.primitives.validation.notEmpty
import org.ddd.primitives.validation.onlyContainNumbers
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

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
        val exc = shouldThrow<ValidationException> {
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
        val exc = shouldThrow<ValidationException> {
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
        val exc = shouldThrow<ValidationException> {
            Customer(
                customerNumber = "123456",
                userName = "asdf",
                emailAddress = "asdf@test@example.com",
            )
        }

        exc.message shouldContain "email should be valid"
    }

    @Test
    internal fun `customer should throw if invalid after copy`() {

        val customer = Customer(
            customerNumber = "123456",
            userName = "asdf",
            emailAddress = "asdf@example.com",
        )

        val exc = shouldThrow<ValidationException> {
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
    notEmpty(items, "Item List must not be empty"),
    must("Billing and shipping address must not be equal") {
        billingAddress != shippingAddress
    }
)

internal data class Customer(
    val customerNumber: String,
    val userName: String,
    val emailAddress: String,
) : Entity<String>(
    notBlank(customerNumber, "Customer number must not be blank"),
    onlyContainNumbers(customerNumber, "Customer number must only consist of numbers"),
    notBlank(userName, "User name must not be blank"),
    conformRegEx(emailAddress, "email should be valid", "^[^@]+@[^@]+\\.[^@]+\$".toRegex())
) {
    override fun businessKey() = customerNumber
}

internal data class Item(
    val articleNo: ArticleNumber,
    val price: Price,
) : Entity<ArticleNumber>() {
    override fun businessKey() = articleNo
}

internal data class ArticleNumber(
    val number: String,
) : SingleValueObject<String>(
    number,
    notBlank(number, "Article number must not be blank"),
    onlyContainNumbers(number, "Article number must only consists of numbers")
)

internal data class Price(
    val price: BigDecimal,
    val currency: Currency,
) : ValueObject(
    greaterThanZero(price, "Price must be greater than Zero")
)

internal data class Address(
    val name: String,
    val street: String,
    val zipCode: String,
    val city: String,
    val countryCode: String?,
) : ValueObject(
    notBlank(name, "Name must not be blank"),
    notBlank(street, "Street must not be blank"),
    notBlank(zipCode, "ZipCode must not be blank"),
    notBlank(city, "City must not be blank"),
    ValueValidation(countryCode, "Country Code needs to be an exiting one") { cc ->
        cc?.let { isoCountryCodes.contains(it) } ?: true
    }
)

val isoCountryCodes: Set<String> = Locale.getISOCountries().asSequence().toSet()
