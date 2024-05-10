package com.gopark.core.service

import com.gopark.core.domain.User
import com.gopark.core.dto.UserDTO
import com.gopark.core.repos.ParkingRepository
import com.gopark.core.repos.RoleRepository
import com.gopark.core.repos.UserRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val parkingRepository: ParkingRepository
) : UserService {

    override fun findAll(): List<UserDTO> {
        val users = userRepository.findAll(Sort.by("id"))
        return users.stream()
                .map { user -> mapToDTO(user, UserDTO()) }
                .toList()
    }

    override fun get(id: Long): UserDTO = userRepository.findById(id)
            .map { user -> mapToDTO(user, UserDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(userDTO: UserDTO): Long {
        val user = User()
        mapToEntity(userDTO, user)
        return userRepository.save(user).id!!
    }

    override fun update(id: Long, userDTO: UserDTO) {
        val user = userRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(userDTO, user)
        userRepository.save(user)
    }

    override fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    private fun mapToDTO(user: User, userDTO: UserDTO): UserDTO {
        userDTO.id = user.id
        userDTO.name = user.name
        userDTO.surname = user.surname
        userDTO.email = user.email
        userDTO.documentNumber = user.documentNumber
        userDTO.role = user.role?.id
        return userDTO
    }

    private fun mapToEntity(userDTO: UserDTO, user: User): User {
        user.name = userDTO.name
        user.surname = userDTO.surname
        user.email = userDTO.email
        user.password = passwordEncoder.encode(userDTO.password)
        user.documentNumber = userDTO.documentNumber
        val role = if (userDTO.role == null) null else roleRepository.findById(userDTO.role!!)
                .orElseThrow { NotFoundException("role not found") }
        user.role = role
        return user
    }

    override fun getReferencedWarning(id: Long): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val user = userRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val ownerParking = parkingRepository.findFirstByOwner(user)
        if (ownerParking != null) {
            referencedWarning.key = "user.parking.owner.referenced"
            referencedWarning.addParam(ownerParking.id)
            return referencedWarning
        }
        return null
    }

    override fun findByEmail(email: String): UserDTO? {
        val user = userRepository.findByEmail(email)
        return user?.let { mapToDTO(it, UserDTO()) }
    }

}
