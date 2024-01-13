package dev.wellington.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.wellington.credit.application.system.dto.request.CreditDto
import dev.wellington.credit.application.system.dto.request.CustomerDto
import dev.wellington.credit.application.system.repository.CreditRepository
import dev.wellington.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@Sql("/scripts/clean-database.sql")
class CreditResourceIT {
    @Autowired private lateinit var creditRepository: CreditRepository
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @Test
    fun `should create a credit and return 201 status`() {
        customerRepository.save(builderCustomerDto().toEntity())
        val creditDto = builderCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(2500.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("24"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").value("2024-01-20"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("wel@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(1000.0))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a credit with dayFirstInstallment with a future date greater than 3 months after the current date and return status 400`() {
        val creditDto = builderCreditDto(dayFirstInstallment = LocalDate.parse("2030-12-30"))
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                .value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class dev.wellington.credit.application.system.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a credit with a customer that does not exist and return status 400`() {
        val creditDto = builderCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                .value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class dev.wellington.credit.application.system.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return the credit list of a customer by id and return 200 status`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        creditRepository.save(builderCreditDto().toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(2500.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value("24"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dayFirstInstallment").value("2024-01-20"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("IN_PROGRESS"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return a customer's credit by id and return status 200`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit = creditRepository.save(builderCreditDto().toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(2500.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("24"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").value("2024-01-20"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not return the credit of a customer who doesn't exist and return status 400`() {
        val credit = creditRepository.save(builderCreditDto().toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                .value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class java.lang.IllegalArgumentException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(2500.00),
        dayFirstInstallment: LocalDate = LocalDate.parse("2024-01-20"),
        numberOfInstallments: Int = 24,
        customerId: Long = 1
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun builderCustomerDto(
        firstName: String = "Wellington",
        lastName: String = "Santos",
        cpf: String = "28475934625",
        email: String = "wel@email.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua Test",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )
}
