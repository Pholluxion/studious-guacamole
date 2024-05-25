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
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/api/payments"],produces = [MediaType.APPLICATION_JSON_VALUE])
@SecurityRequirement(name = "bearer-jwt")
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
class PaymentResource(
    private val paymentService: PaymentService
) {

    @GetMapping
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun getAllPayments(): ResponseEntity<List<PaymentDTO>> =
            ResponseEntity.ok(paymentService.findAll())

    @GetMapping("/{id}")
    fun getPayment(@PathVariable(name = "id") id: Int): ResponseEntity<PaymentDTO> =
            ResponseEntity.ok(paymentService.get(id))

    @PostMapping
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    @ApiResponse(responseCode = "201")
    fun createPayment(@RequestBody @Valid paymentDTO: PaymentDTO): ResponseEntity<Int> {
        val createdId = paymentService.create(paymentDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun updatePayment(@PathVariable(name = "id") id: Int, @RequestBody @Valid
            paymentDTO: PaymentDTO): ResponseEntity<Int> {
        paymentService.update(id, paymentDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun deletePayment(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        paymentService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
