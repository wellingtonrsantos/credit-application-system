package dev.wellington.credit.application.system.controller

import dev.wellington.credit.application.system.dto.request.CreditDto
import dev.wellington.credit.application.system.dto.response.CreditView
import dev.wellington.credit.application.system.dto.response.CreditViewList
import dev.wellington.credit.application.system.entity.Credit
import dev.wellington.credit.application.system.service.impl.CreditService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @Operation(summary = "Save a new customer credit")
    @PostMapping
    fun save(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CreditView(credit))
    }

    @Operation(summary = "Search all the credits of a customer by id")
    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
            ResponseEntity<List<CreditViewList>> {
        val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomer(customerId)
            .stream().map { credit -> CreditViewList(credit) }.collect(Collectors.toList())
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(creditViewList)
    }

    @Operation(summary = "Search for a specific credit by code and customer id")
    @GetMapping("{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID) : ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId , creditCode)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CreditView(credit))
    }
}
