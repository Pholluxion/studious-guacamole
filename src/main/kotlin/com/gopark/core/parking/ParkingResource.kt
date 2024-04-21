package com.gopark.core.parking

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/parkings"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class ParkingResource(
    private val parkingService: ParkingService,
    private val parkingAssembler: ParkingAssembler
) {

    @GetMapping
    fun getAllParkings(@RequestParam(name = "filter", required = false) filter: String?):
            ResponseEntity<CollectionModel<EntityModel<ParkingDTO>>> {
        val parkingDTOs = parkingService.findAll(filter)
        return ResponseEntity.ok(parkingAssembler.toCollectionModel(parkingDTOs))
    }

    @GetMapping("/{parkingId}")
    fun getParking(@PathVariable(name = "parkingId") parkingId: Int):
            ResponseEntity<EntityModel<ParkingDTO>> {
        val parkingDTO = parkingService.get(parkingId)
        return ResponseEntity.ok(parkingAssembler.toModel(parkingDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createParking(@RequestBody @Valid parkingDTO: ParkingDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        val createdParkingId = parkingService.create(parkingDTO)
        return ResponseEntity(parkingAssembler.toSimpleModel(createdParkingId), HttpStatus.CREATED)
    }

    @PutMapping("/{parkingId}")
    fun updateParking(@PathVariable(name = "parkingId") parkingId: Int, @RequestBody @Valid
            parkingDTO: ParkingDTO): ResponseEntity<EntityModel<SimpleValue<Int>>> {
        parkingService.update(parkingId, parkingDTO)
        return ResponseEntity.ok(parkingAssembler.toSimpleModel(parkingId))
    }

    @DeleteMapping("/{parkingId}")
    @ApiResponse(responseCode = "204")
    fun deleteParking(@PathVariable(name = "parkingId") parkingId: Int): ResponseEntity<Unit> {
        val referencedWarning = parkingService.getReferencedWarning(parkingId)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        parkingService.delete(parkingId)
        return ResponseEntity.noContent().build()
    }

}
