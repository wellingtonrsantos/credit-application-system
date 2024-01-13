package dev.wellington.credit.application.system.dto.request

import dev.wellington.credit.application.system.entity.Address
import dev.wellington.credit.application.system.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "The first name cannot be null or empty!") val firstName: String,
    @field:NotEmpty(message = "The last name cannot be null or empty!!") val lastName: String,
    @field:NotEmpty(message = "The cpf cannot be null or empty!!") @field:CPF(message = "This invalid CPF") val cpf: String,
    @field:NotNull(message = "The income value cannot be null!") val income: BigDecimal,
    @field:NotEmpty(message = "The email cannot be null or empty!!") @field:Email(message = "Invalid email") val email: String,
    @field:NotEmpty(message = "The password cannot be null or empty!!") val password: String,
    @field:NotEmpty(message = "The zip code cannot be null or empty!!") val zipCode: String,
    @field:NotEmpty(message = "The street cannot be null or empty!!") val street: String
) {

    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(zipCode = this.zipCode, street = this.street)
    )
}