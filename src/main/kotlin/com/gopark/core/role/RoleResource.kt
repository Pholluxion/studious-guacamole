package com.gopark.core.role

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
    value = ["/api/roles"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class RoleResource(
    private val roleService: RoleService,
    private val roleAssembler: RoleAssembler
) {

    @GetMapping
    fun getAllRoles(): ResponseEntity<CollectionModel<EntityModel<RoleDTO>>> {
        val roleDTOs = roleService.findAll()
        return ResponseEntity.ok(roleAssembler.toCollectionModel(roleDTOs))
    }

    @GetMapping("/{id}")
    fun getRole(@PathVariable(name = "id") id: Long): ResponseEntity<EntityModel<RoleDTO>> {
        val roleDTO = roleService.get(id)
        return ResponseEntity.ok(roleAssembler.toModel(roleDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createRole(@RequestBody @Valid roleDTO: RoleDTO):
            ResponseEntity<EntityModel<SimpleValue<Long>>> {
        val createdId = roleService.create(roleDTO)
        return ResponseEntity(roleAssembler.toSimpleModel(createdId), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateRole(@PathVariable(name = "id") id: Long, @RequestBody @Valid roleDTO: RoleDTO):
            ResponseEntity<EntityModel<SimpleValue<Long>>> {
        roleService.update(id, roleDTO)
        return ResponseEntity.ok(roleAssembler.toSimpleModel(id))
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteRole(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        val referencedWarning = roleService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        roleService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
