package com.gopark.core.fee

import com.gopark.core.model.SimpleValue
import com.gopark.core.security.UserRoles
import com.gopark.core.util.ReferencedException
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import java.lang.Void
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
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/fees"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class FeeResource(
    private val feeService: FeeService,
    private val feeAssembler: FeeAssembler
) {

    @GetMapping
    fun getAllFees(): ResponseEntity<CollectionModel<EntityModel<FeeDTO>>> {
        val feeDTOs = feeService.findAll()
        return ResponseEntity.ok(feeAssembler.toCollectionModel(feeDTOs))
    }

    @GetMapping("/{feeId}")
    fun getFee(@PathVariable(name = "feeId") feeId: Int): ResponseEntity<EntityModel<FeeDTO>> {
        val feeDTO = feeService.get(feeId)
        return ResponseEntity.ok(feeAssembler.toModel(feeDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createFee(@RequestBody @Valid feeDTO: FeeDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        val createdFeeId = feeService.create(feeDTO)
        return ResponseEntity(feeAssembler.toSimpleModel(createdFeeId), HttpStatus.CREATED)
    }

    @PutMapping("/{feeId}")
    fun updateFee(@PathVariable(name = "feeId") feeId: Int, @RequestBody @Valid feeDTO: FeeDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        feeService.update(feeId, feeDTO)
        return ResponseEntity.ok(feeAssembler.toSimpleModel(feeId))
    }

    @DeleteMapping("/{feeId}")
    @ApiResponse(responseCode = "204")
    fun deleteFee(@PathVariable(name = "feeId") feeId: Int): ResponseEntity<Void> {
        val referencedWarning = feeService.getReferencedWarning(feeId)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        feeService.delete(feeId)
        return ResponseEntity.noContent().build()
    }

}
