package dev.wellington.credit.application.system.service

import dev.wellington.credit.application.system.entity.Address
import dev.wellington.credit.application.system.entity.Credit
import dev.wellington.credit.application.system.entity.Customer
import dev.wellington.credit.application.system.enummeration.Status
import dev.wellington.credit.application.system.exception.BusinessException
import dev.wellington.credit.application.system.repository.CreditRepository
import dev.wellington.credit.application.system.service.impl.CreditService
import dev.wellington.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.collections.List

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should create credit`(){
        val customer: Customer = buildCustomer()
        val expectedCredit: Credit = buildCredit(customer = customer)
        every { customerService.findById(expectedCredit.customer?.id!!) } returns customer
        every { creditRepository.save(any()) } returns expectedCredit

        val actual: Credit = creditService.save(expectedCredit)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(expectedCredit)
        verify(exactly = 1) { creditRepository.save(expectedCredit) }
    }

    @Test
    fun `should not save credit if the date is older than 3 months`(){
        val customer: Customer = buildCustomer()
        val expectedCredit: Credit = buildCredit(customer = customer,
            dayFirstInstallment = LocalDate.parse("2025-12-30"))
        every { customerService.findById(expectedCredit.customer?.id!!) } returns customer

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.save(expectedCredit) }
            .withMessage("The date of the first installment must be no later " +
                    "than 3 months after the current date!")
    }

    @Test
    fun `should return all of a customer's credits by id`(){
        val customer: Customer = buildCustomer()
        val expectedCredit: Credit = buildCredit(customer = customer)
        every { creditRepository.findAllByCustomerId(any()) } returns listOf(expectedCredit)

        val actualList: List<Credit> = creditService.findAllByCustomer(1L)

        Assertions.assertThat(actualList).isNotEmpty
        Assertions.assertThat(actualList).size().isEqualTo(1)
        Assertions.assertThat(actualList).contains(expectedCredit)
    }

    @Test
    fun `should return the credit for the code`(){
        val customer: Customer = buildCustomer()
        val expectedCredit: Credit = buildCredit(customer = customer,
            creditCode = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29"))
        every { creditRepository.findByCreditCode(any()) } returns expectedCredit

        val actual: Credit = creditService.findByCreditCode(1L, expectedCredit.creditCode)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(expectedCredit)
    }

    @Test
    fun `should not return the credit if it does not exist`(){
        val creditCode = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")

        every { creditRepository.findByCreditCode(any()) } returns null

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(1L, creditCode) }
            .withMessage("Credit code $creditCode not found")
    }

    @Test
    fun `should not return the credit if it is not from the customer informed`(){
        val creditCode = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val credit: Credit = buildCredit(customer = buildCustomer(), creditCode = creditCode)

        every { creditRepository.findByCreditCode(any()) } returns credit

        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { creditService.findByCreditCode(2L, creditCode) }
            .withMessage("Contact admin")
    }

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal.valueOf(2500.0),
        dayFirstInstallment: LocalDate = LocalDate.parse("2024-01-20"),
        numberOfInstallments: Int = 24,
        status: Status = Status.IN_PROGRESS,
        customer: Customer? = null
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        status = status,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Wellington",
        lastName: String = "Santos",
        cpf: String = "28475934625",
        email: String = "wel@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua Test",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )
}
