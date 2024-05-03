package com.gopark.core.rest

import com.gopark.core.dto.PaymentDTO
import com.gopark.core.service.PaymentService
import com.gopark.core.util.UserRoles
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/payments"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class PaymentResource(
    private val paymentService: PaymentService
) {

    @GetMapping
    fun getAllPayments(): ResponseEntity<List<PaymentDTO>> =
            ResponseEntity.ok(paymentService.findAll())

    @GetMapping("/{id}")
    fun getPayment(@PathVariable(name = "id") id: Int): ResponseEntity<PaymentDTO> =
            ResponseEntity.ok(paymentService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createPayment(@RequestBody @Valid paymentDTO: PaymentDTO): ResponseEntity<Int> {
        val createdId = paymentService.create(paymentDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePayment(@PathVariable(name = "id") id: Int, @RequestBody @Valid
            paymentDTO: PaymentDTO): ResponseEntity<Int> {
        paymentService.update(id, paymentDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deletePayment(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        paymentService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
