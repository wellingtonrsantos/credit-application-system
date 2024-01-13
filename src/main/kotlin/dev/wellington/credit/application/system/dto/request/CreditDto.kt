package dev.wellington.credit.application.system.dto.request

import dev.wellington.credit.application.system.entity.Credit
import dev.wellington.credit.application.system.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "The credit value cannot be null!") val creditValue: BigDecimal,
    @field:Future @NotNull(message = "The day first installment cannot be null!") val dayFirstInstallment: LocalDate,
    @field:NotNull(message = "The number of installments cannot be null!")
    @field:Max(value = 48, message = "The maximum number of installments allowed is up to 48!")
    @field:Min(value = 1, message = "The minimum number of installments allowed is 1!")
    val numberOfInstallments: Int,
    @field:NotNull(message = "The customer id cannot be null!") val customerId: Long
){

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )

}
