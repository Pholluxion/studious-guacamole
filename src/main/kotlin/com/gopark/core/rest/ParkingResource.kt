package com.gopark.core.rest

import com.gopark.core.dto.ParkingDTO
import com.gopark.core.service.ParkingService
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
    value = ["/api/parkings"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class ParkingResource(
    private val parkingService: ParkingService
) {

    @GetMapping
    fun getAllParkings(): ResponseEntity<List<ParkingDTO>> =
            ResponseEntity.ok(parkingService.findAll())

    @GetMapping("/{id}")
    fun getParking(@PathVariable(name = "id") id: Int): ResponseEntity<ParkingDTO> =
            ResponseEntity.ok(parkingService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createParking(@RequestBody @Valid parkingDTO: ParkingDTO): ResponseEntity<Int> {
        val createdId = parkingService.create(parkingDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateParking(@PathVariable(name = "id") id: Int, @RequestBody @Valid
            parkingDTO: ParkingDTO): ResponseEntity<Int> {
        parkingService.update(id, parkingDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteParking(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        val referencedWarning = parkingService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        parkingService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
