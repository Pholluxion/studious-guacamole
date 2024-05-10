package com.gopark.core.rest

import com.gopark.core.dto.RegistrationDTO
import com.gopark.core.service.RegistrationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
class RegistrationResource(
    private val registrationService: RegistrationService
) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid registrationDTO: RegistrationDTO):
            ResponseEntity<Unit> {
        registrationService.register(registrationDTO)
        return ResponseEntity.ok().build()
    }

}
