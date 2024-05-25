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
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/api/vehicleTypes"],produces = [MediaType.APPLICATION_JSON_VALUE])
@SecurityRequirement(name = "bearer-jwt")
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
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
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    @ApiResponse(responseCode = "201")
    fun createVehicleType(@RequestBody @Valid vehicleTypeDTO: VehicleTypeDTO): ResponseEntity<Int> {
        val createdId = vehicleTypeService.create(vehicleTypeDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun updateVehicleType(@PathVariable(name = "id") id: Int, @RequestBody @Valid
            vehicleTypeDTO: VehicleTypeDTO): ResponseEntity<Int> {
        vehicleTypeService.update(id, vehicleTypeDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun deleteVehicleType(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        val referencedWarning = vehicleTypeService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        vehicleTypeService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
