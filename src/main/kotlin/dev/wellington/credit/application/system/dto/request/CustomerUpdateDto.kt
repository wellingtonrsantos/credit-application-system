package dev.wellington.credit.application.system.dto.request

import dev.wellington.credit.application.system.entity.Address
import dev.wellington.credit.application.system.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "The first name cannot be null or empty!!") val firstName: String,
    @field:NotEmpty(message = "The last name cannot be null or empty!!") val lastName: String,
    @field:NotNull(message = "The income value cannot be null!") val income: BigDecimal,
    @field:NotEmpty(message = "The zip code cannot be null or empty!!") val zipCode: String,
    @field:NotEmpty(message = "The street cannot be null or empty!!") val street: String
) {
    fun toEntity(customer: Customer): Customer {
        return Customer(
            id = customer.id,
            cpf = customer.cpf,
            email = customer.email,
            password = customer.password,
            firstName = this.firstName,
            lastName = this.lastName,
            income = this.income,
            address = Address(zipCode = this.zipCode, street = this.street)
        )
    }
}