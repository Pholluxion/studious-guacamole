package com.gopark.core.security

import com.gopark.core.role.RoleRepository
import com.gopark.core.user.User
import com.gopark.core.user.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {

    fun register(registrationRequest: RegistrationRequest) {
        log.info("registering new user: {}", registrationRequest.email)

        val user = User()
        user.name = registrationRequest.name
        user.surname = registrationRequest.surname
        user.email = registrationRequest.email
        user.password = passwordEncoder.encode(registrationRequest.password)
        user.documentNumber = registrationRequest.documentNumber
        user.role = roleRepository.findByName(UserRoles.SU)
        userRepository.save(user)
    }

    fun emailExists(email: String?): Boolean = userRepository.existsByEmailIgnoreCase(email)


    companion object {

        val log: Logger = LoggerFactory.getLogger(RegistrationService::class.java)

    }

}
