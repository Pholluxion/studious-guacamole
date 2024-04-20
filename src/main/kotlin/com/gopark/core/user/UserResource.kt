package com.gopark.core.user

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
@SecurityRequirement(name = "bearer-jwt")
class UserResource(
    private val userService: UserService,
    private val userAssembler: UserAssembler
) {

    @GetMapping
    fun getAllUsers(@RequestParam(name = "filter", required = false) filter: String?):
            ResponseEntity<CollectionModel<EntityModel<UserDTO>>> {
        val userDTOs = userService.findAll(filter)
        return ResponseEntity.ok(userAssembler.toCollectionModel(userDTOs))
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable(name = "id") id: Long): ResponseEntity<EntityModel<UserDTO>> {
        val userDTO = userService.get(id)
        return ResponseEntity.ok(userAssembler.toModel(userDTO))
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createUser(@RequestBody @Valid userDTO: UserDTO):
            ResponseEntity<EntityModel<SimpleValue<Long>>> {
        val createdId = userService.create(userDTO)
        return ResponseEntity(userAssembler.toSimpleModel(createdId), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable(name = "id") id: Long, @RequestBody @Valid userDTO: UserDTO):
            ResponseEntity<EntityModel<SimpleValue<Long>>> {
        userService.update(id, userDTO)
        return ResponseEntity.ok(userAssembler.toSimpleModel(id))
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteUser(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        val referencedWarning = userService.getReferencedWarning(id)
        if (referencedWarning != null) {
            throw ReferencedException(referencedWarning)
        }
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
