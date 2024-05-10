package com.gopark.core.rest

import com.gopark.core.dto.RoleDTO
import com.gopark.core.service.RoleService
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
@RequestMapping(value = ["/api/roles"],produces = [MediaType.APPLICATION_JSON_VALUE])
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
class RoleResource(
    private val roleService: RoleService
) {

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<RoleDTO>> = ResponseEntity.ok(roleService.findAll())

    @GetMapping("/{id}")
    fun getRole(@PathVariable(name = "id") id: Long): ResponseEntity<RoleDTO> =
            ResponseEntity.ok(roleService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createRole(@RequestBody @Valid roleDTO: RoleDTO): ResponseEntity<Long> {
        val createdId = roleService.create(roleDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateRole(@PathVariable(name = "id") id: Long, @RequestBody @Valid roleDTO: RoleDTO):
            ResponseEntity<Long> {
        roleService.update(id, roleDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteRole(@PathVariable(name = "id") id: Long): ResponseEntity<Unit> {
        val referencedWarning = roleService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        roleService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
