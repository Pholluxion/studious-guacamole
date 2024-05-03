package com.gopark.core.rest

import com.gopark.core.dto.VehicleTypeDTO
import com.gopark.core.service.VehicleTypeService
import com.gopark.core.util.ReferencedException
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
    value = ["/api/vehicleTypes"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class VehicleTypeResource(
    private val vehicleTypeService: VehicleTypeService
) {

    @GetMapping
    fun getAllVehicleTypes(): ResponseEntity<List<VehicleTypeDTO>> =
            ResponseEntity.ok(vehicleTypeService.findAll())

    @GetMapping("/{id}")
    fun getVehicleType(@PathVariable(name = "id") id: Int): ResponseEntity<VehicleTypeDTO> =
            ResponseEntity.ok(vehicleTypeService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createVehicleType(@RequestBody @Valid vehicleTypeDTO: VehicleTypeDTO): ResponseEntity<Int> {
        val createdId = vehicleTypeService.create(vehicleTypeDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateVehicleType(@PathVariable(name = "id") id: Int, @RequestBody @Valid
            vehicleTypeDTO: VehicleTypeDTO): ResponseEntity<Int> {
        vehicleTypeService.update(id, vehicleTypeDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteVehicleType(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        val referencedWarning = vehicleTypeService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        vehicleTypeService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
