package com.gopark.core.rest

import com.gopark.core.dto.SpotDTO
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
    value = ["/api/spots"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class SpotResource(
    private val spotService: SpotService
) {

    @GetMapping
    fun getAllSpots(): ResponseEntity<List<SpotDTO>> = ResponseEntity.ok(spotService.findAll())

    @GetMapping("/{id}")
    fun getSpot(@PathVariable(name = "id") id: Int): ResponseEntity<SpotDTO> =
            ResponseEntity.ok(spotService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createSpot(@RequestBody @Valid spotDTO: SpotDTO): ResponseEntity<Int> {
        val createdId = spotService.create(spotDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateSpot(@PathVariable(name = "id") id: Int, @RequestBody @Valid spotDTO: SpotDTO):
            ResponseEntity<Int> {
        spotService.update(id, spotDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteSpot(@PathVariable(name = "id") id: Int): ResponseEntity<Unit> {
        val referencedWarning = spotService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        spotService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
