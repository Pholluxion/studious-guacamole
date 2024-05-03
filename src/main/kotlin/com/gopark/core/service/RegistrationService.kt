package com.gopark.core.service

import com.gopark.core.domain.User
import com.gopark.core.dto.RegistrationDTO
import com.gopark.core.repos.RoleRepository
import com.gopark.core.repos.UserRepository
import com.gopark.core.util.UserRoles
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

    fun register(registrationDTO: RegistrationDTO) {
        log.info("registering new user: {}", registrationDTO.email)

        val user = User()
        user.name = registrationDTO.name
        user.surname = registrationDTO.surname
        user.email = registrationDTO.email
        user.password = passwordEncoder.encode(registrationDTO.password)
        user.documentNumber = registrationDTO.documentNumber
        // assign default role
        user.role = roleRepository.findByName(UserRoles.SU)
        userRepository.save(user)
    }

    fun emailExists(email: String?): Boolean = userRepository.existsByEmailIgnoreCase(email)


    companion object {

        val log: Logger = LoggerFactory.getLogger(RegistrationService::class.java)

    }

}
