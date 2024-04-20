package com.gopark.core.user

import com.gopark.core.parking.ParkingRepository
import com.gopark.core.role.RoleRepository
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

    override fun findAll(filter: String?): List<UserDTO> {
        var users: List<User>
        val sort = Sort.by("id")
        if (filter != null) {
            users = userRepository.findAllById(filter.toLongOrNull(), sort)
        } else {
            users = userRepository.findAll(sort)
        }
        return users.stream()
                .map { user -> mapToDTO(user, UserDTO()) }
                .toList()
    }

    override fun `get`(id: Long): UserDTO = userRepository.findById(id)
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
            referencedWarning.addParam(ownerParking.parkingId)
            return referencedWarning
        }
        return null
    }

}
