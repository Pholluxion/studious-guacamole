package com.gopark.core.security

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class AuthenticationResource(
    private val authenticationManager: AuthenticationManager,
    private val jwtUserDetailsService: JwtUserDetailsService,
    private val jwtTokenService: JwtTokenService
) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody @Valid authenticationRequest: AuthenticationRequest):
            AuthenticationResponse {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(authenticationRequest.email,
                    authenticationRequest.password))
        } catch (ex: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.email!!)
        val authenticationResponse = AuthenticationResponse()
        authenticationResponse.accessToken = jwtTokenService.generateToken(userDetails)
        return authenticationResponse
    }

}
