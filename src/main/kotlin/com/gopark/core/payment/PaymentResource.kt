package com.gopark.core.payment

import com.gopark.core.model.SimpleValue
import com.gopark.core.security.UserRoles
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/payments"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class PaymentResource(
    private val paymentService: PaymentService,
    private val paymentAssembler: PaymentAssembler
) {

    @GetMapping
    fun getAllPayments(@RequestParam(name = "filter", required = false) filter: String?):
            ResponseEntity<CollectionModel<EntityModel<PaymentDTO>>> {
        val paymentDTOs = paymentService.findAll(filter)
        return ResponseEntity.ok(paymentAssembler.toCollectionModel(paymentDTOs))
    }

    @GetMapping("/{paymentId}")
    fun getPayment(@PathVariable(name = "paymentId") paymentId: Int):
            ResponseEntity<EntityModel<PaymentDTO>> {
        val paymentDTO = paymentService.get(paymentId)
        return ResponseEntity.ok(paymentAssembler.toModel(paymentDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createPayment(@RequestBody @Valid paymentDTO: PaymentDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        val createdPaymentId = paymentService.create(paymentDTO)
        return ResponseEntity(paymentAssembler.toSimpleModel(createdPaymentId), HttpStatus.CREATED)
    }

    @PutMapping("/{paymentId}")
    fun updatePayment(@PathVariable(name = "paymentId") paymentId: Int, @RequestBody @Valid
            paymentDTO: PaymentDTO): ResponseEntity<EntityModel<SimpleValue<Int>>> {
        paymentService.update(paymentId, paymentDTO)
        return ResponseEntity.ok(paymentAssembler.toSimpleModel(paymentId))
    }

    @DeleteMapping("/{paymentId}")
    @ApiResponse(responseCode = "204")
    fun deletePayment(@PathVariable(name = "paymentId") paymentId: Int): ResponseEntity<Unit> {
        paymentService.delete(paymentId)
        return ResponseEntity.noContent().build()
    }

}
