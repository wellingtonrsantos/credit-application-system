package dev.wellington.credit.application.system.dto.response

import dev.wellington.credit.application.system.entity.Credit
import dev.wellington.credit.application.system.enummeration.Status
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int,
    val dayFirstInstallment: LocalDate,
    val status: Status = Status.IN_PROGRESS
){

    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        dayFirstInstallment = credit.dayFirstInstallment,
        status = credit.status
    )
}
