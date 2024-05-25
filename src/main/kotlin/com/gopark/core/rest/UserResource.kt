package com.gopark.core.rest

import com.gopark.core.dto.UserDTO
import com.gopark.core.service.UserService
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
@RequestMapping(value = ["/api/users"],produces = [MediaType.APPLICATION_JSON_VALUE])
@SecurityRequirement(name = "bearer-jwt")
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
class UserResource(
    private val userService: UserService
) {

    @GetMapping
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> = ResponseEntity.ok(userService.findAll())

    @GetMapping("/{id}")
    fun getUser(@PathVariable(name = "id") id: Long): ResponseEntity<UserDTO> =
            ResponseEntity.ok(userService.get(id))

    @PostMapping
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    @ApiResponse(responseCode = "201")
    fun createUser(@RequestBody @Valid userDTO: UserDTO): ResponseEntity<Long> {
        val createdId = userService.create(userDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun updateUser(@PathVariable(name = "id") id: Long, @RequestBody @Valid userDTO: UserDTO):
            ResponseEntity<Long> {
        userService.update(id, userDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + UserRoles.SU + "')")
    fun deleteUser(@PathVariable(name = "id") id: Long): ResponseEntity<Unit> {

        try {
            userService.delete(id)
        } catch (e: ReferencedException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        return ResponseEntity.noContent().build()
    }

}
