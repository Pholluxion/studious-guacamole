package com.gopark.core.vehicle_type

import com.gopark.core.model.SimpleValue
import com.gopark.core.security.UserRoles
import com.gopark.core.util.ReferencedException
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
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/vehicleTypes"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class VehicleTypeResource(
    private val vehicleTypeService: VehicleTypeService,
    private val vehicleTypeAssembler: VehicleTypeAssembler
) {

    @GetMapping
    fun getAllVehicleTypes(): ResponseEntity<CollectionModel<EntityModel<VehicleTypeDTO>>> {
        val vehicleTypeDTOs = vehicleTypeService.findAll()
        return ResponseEntity.ok(vehicleTypeAssembler.toCollectionModel(vehicleTypeDTOs))
    }

    @GetMapping("/{vehicleTypeId}")
    fun getVehicleType(@PathVariable(name = "vehicleTypeId") vehicleTypeId: Int):
            ResponseEntity<EntityModel<VehicleTypeDTO>> {
        val vehicleTypeDTO = vehicleTypeService.get(vehicleTypeId)
        return ResponseEntity.ok(vehicleTypeAssembler.toModel(vehicleTypeDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createVehicleType(@RequestBody @Valid vehicleTypeDTO: VehicleTypeDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        val createdVehicleTypeId = vehicleTypeService.create(vehicleTypeDTO)
        return ResponseEntity(vehicleTypeAssembler.toSimpleModel(createdVehicleTypeId),
                HttpStatus.CREATED)
    }

    @PutMapping("/{vehicleTypeId}")
    fun updateVehicleType(@PathVariable(name = "vehicleTypeId") vehicleTypeId: Int, @RequestBody
            @Valid vehicleTypeDTO: VehicleTypeDTO): ResponseEntity<EntityModel<SimpleValue<Int>>> {
        vehicleTypeService.update(vehicleTypeId, vehicleTypeDTO)
        return ResponseEntity.ok(vehicleTypeAssembler.toSimpleModel(vehicleTypeId))
    }

    @DeleteMapping("/{vehicleTypeId}")
    @ApiResponse(responseCode = "204")
    fun deleteVehicleType(@PathVariable(name = "vehicleTypeId") vehicleTypeId: Int):
            ResponseEntity<Unit> {
        val referencedWarning = vehicleTypeService.getReferencedWarning(vehicleTypeId)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        vehicleTypeService.delete(vehicleTypeId)
        return ResponseEntity.noContent().build()
    }

}
