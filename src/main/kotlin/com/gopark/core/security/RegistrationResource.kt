package com.gopark.core.security

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class RegistrationResource(
    private val registrationService: RegistrationService
) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid registrationRequest: RegistrationRequest):
            ResponseEntity<Unit> {
        registrationService.register(registrationRequest)
        return ResponseEntity.ok().build()
    }

}
