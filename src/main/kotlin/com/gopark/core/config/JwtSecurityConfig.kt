package com.gopark.core.config

import java.lang.Exception
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class JwtSecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // please note: existing hashes must contain {bcrypt} prefix
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration):
            AuthenticationManager = authenticationConfiguration.authenticationManager

    @Bean
    @Throws(Exception::class)
    fun jwtFilterChain(http: HttpSecurity): SecurityFilterChain =
            http.cors(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize -> authorize.anyRequest().permitAll() }
            .sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

}
