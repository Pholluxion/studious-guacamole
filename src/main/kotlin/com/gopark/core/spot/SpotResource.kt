package com.gopark.core.spot

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
    value = ["/api/spots"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class SpotResource(
    private val spotService: SpotService,
    private val spotAssembler: SpotAssembler
) {

    @GetMapping
    fun getAllSpots(@RequestParam(name = "filter", required = false) filter: String?):
            ResponseEntity<CollectionModel<EntityModel<SpotDTO>>> {
        val spotDTOs = spotService.findAll(filter)
        return ResponseEntity.ok(spotAssembler.toCollectionModel(spotDTOs))
    }

    @GetMapping("/{spotId}")
    fun getSpot(@PathVariable(name = "spotId") spotId: Int): ResponseEntity<EntityModel<SpotDTO>> {
        val spotDTO = spotService.get(spotId)
        return ResponseEntity.ok(spotAssembler.toModel(spotDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createSpot(@RequestBody @Valid spotDTO: SpotDTO):
            ResponseEntity<EntityModel<SimpleValue<Int>>> {
        val createdSpotId = spotService.create(spotDTO)
        return ResponseEntity(spotAssembler.toSimpleModel(createdSpotId), HttpStatus.CREATED)
    }

    @PutMapping("/{spotId}")
    fun updateSpot(@PathVariable(name = "spotId") spotId: Int, @RequestBody @Valid
            spotDTO: SpotDTO): ResponseEntity<EntityModel<SimpleValue<Int>>> {
        spotService.update(spotId, spotDTO)
        return ResponseEntity.ok(spotAssembler.toSimpleModel(spotId))
    }

    @DeleteMapping("/{spotId}")
    @ApiResponse(responseCode = "204")
    fun deleteSpot(@PathVariable(name = "spotId") spotId: Int): ResponseEntity<Unit> {
        val referencedWarning = spotService.getReferencedWarning(spotId)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        spotService.delete(spotId)
        return ResponseEntity.noContent().build()
    }

}
