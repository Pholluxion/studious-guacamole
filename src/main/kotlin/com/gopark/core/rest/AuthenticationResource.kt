package com.gopark.core.rest

import com.gopark.core.model.AuthenticationRequest
import com.gopark.core.model.AuthenticationResponse
import com.gopark.core.service.JwtTokenService
import com.gopark.core.service.JwtUserDetailsService
import com.gopark.core.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@CrossOrigin(maxAge = 3600, origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
class AuthenticationResource(
        private val authenticationManager: AuthenticationManager,
        private val jwtUserDetailsService: JwtUserDetailsService,
        private val jwtTokenService: JwtTokenService,
        private val userService: UserService
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
        authenticationResponse.user = userService.findByEmail(authenticationRequest.email!!)

        return authenticationResponse
    }

}
