package com.gopark.core.security

import jakarta.validation.Valid
import java.lang.Void
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
            ResponseEntity<Void> {
        registrationService.register(registrationRequest)
        return ResponseEntity.ok().build()
    }

}