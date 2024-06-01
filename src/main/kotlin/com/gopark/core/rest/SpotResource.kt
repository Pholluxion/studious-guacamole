package com.gopark.core.rest

import com.gopark.core.dto.SpotDTO
import com.gopark.core.service.ParkingService
import com.gopark.core.service.SpotService
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
@RequestMapping(value = ["/api/spots"], produces = [MediaType.APPLICATION_JSON_VALUE])
@SecurityRequirement(name = "bearer-jwt")
@CrossOrigin(
    maxAge = 3600,
    origins = ["*"],
    allowedHeaders = ["*"],
    methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE]
)
class SpotResource(
    private val spotService: SpotService,
    private val parkingService: ParkingService
) {

    @GetMapping
    fun getAllSpots(): ResponseEntity<List<SpotDTO>> = ResponseEntity.ok(spotService.findAll())

    @GetMapping("/{id}")
    fun getSpot(@PathVariable(name = "id") id: Int): ResponseEntity<SpotDTO> =
        ResponseEntity.ok(spotService.get(id))

    //get spots by license plate
    @GetMapping("/license/{licensePlate}")
    fun getSpotsByLicensePlate(@PathVariable(name = "licensePlate") licensePlate: String): ResponseEntity<SpotDTO> {
        try {
            val spots = spotService.getByLicensePlate(licensePlate)

            return ResponseEntity.ok(spots)

        } catch (e: Exception) {
            return ResponseEntity.noContent().build()
        }

    }

    @GetMapping("/parking/{parkingId}")
    fun getSpotsByParkingId(@PathVariable(name = "parkingId") parkingId: Int): ResponseEntity<List<SpotDTO>> {
        try {
            parkingService.get(parkingId)

            val spots = spotService.findAllByParkingId(parkingId)

            if (spots.isEmpty()) {
                return ResponseEntity.noContent().build()
            }

            return ResponseEntity.ok(spots)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }

    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    @ApiResponse(responseCode = "201")
    fun createSpot(@RequestBody @Valid spotDTO: SpotDTO): ResponseEntity<Int> {
        val createdId = spotService.create(spotDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun updateSpot(@PathVariable(name = "id") id: Int, @RequestBody @Valid spotDTO: SpotDTO):
            ResponseEntity<Int> {
        spotService.update(id, spotDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun deleteSpot(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        val referencedWarning = spotService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        spotService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
