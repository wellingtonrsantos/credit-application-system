package dev.wellington.credit.application.system.controller

import dev.wellington.credit.application.system.dto.request.CustomerDto
import dev.wellington.credit.application.system.dto.request.CustomerUpdateDto
import dev.wellington.credit.application.system.dto.response.CustomerView
import dev.wellington.credit.application.system.entity.Customer
import dev.wellington.credit.application.system.service.impl.CustomerService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerService
) {

    @Operation(summary = "Save a new customer")
    @PostMapping
    fun save(@RequestBody @Valid customerDto: CustomerDto) : ResponseEntity<CustomerView> {
        val savedCustomer: Customer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CustomerView(savedCustomer))
    }

    @Operation(summary = "Get a customer by id")
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CustomerView(customer))
    }

    @Operation(summary = "Delete a customer by id")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = this.customerService.delete(id)

    @Operation(summary = "Update a customer by id")
    @PatchMapping
    fun update(@RequestParam(value = "customerId") id: Long,
               @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        val customerUpdated = this.customerService.save(customerUpdateDto.toEntity(customer))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CustomerView(customerUpdated))
    }
}
