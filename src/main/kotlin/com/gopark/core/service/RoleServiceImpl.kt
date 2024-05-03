package com.gopark.core.service

import com.gopark.core.domain.Role
import com.gopark.core.dto.RoleDTO
import com.gopark.core.repos.RoleRepository
import com.gopark.core.repos.UserRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository
) : RoleService {

    override fun findAll(): List<RoleDTO> {
        val roles = roleRepository.findAll(Sort.by("id"))
        return roles.stream()
                .map { role -> mapToDTO(role, RoleDTO()) }
                .toList()
    }

    override fun get(id: Long): RoleDTO = roleRepository.findById(id)
            .map { role -> mapToDTO(role, RoleDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(roleDTO: RoleDTO): Long {
        val role = Role()
        mapToEntity(roleDTO, role)
        return roleRepository.save(role).id!!
    }

    override fun update(id: Long, roleDTO: RoleDTO) {
        val role = roleRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(roleDTO, role)
        roleRepository.save(role)
    }

    override fun delete(id: Long) {
        roleRepository.deleteById(id)
    }

    private fun mapToDTO(role: Role, roleDTO: RoleDTO): RoleDTO {
        roleDTO.id = role.id
        roleDTO.name = role.name
        return roleDTO
    }

    private fun mapToEntity(roleDTO: RoleDTO, role: Role): Role {
        role.name = roleDTO.name
        return role
    }

    override fun nameExists(name: String?): Boolean = roleRepository.existsByNameIgnoreCase(name)

    override fun getReferencedWarning(id: Long): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val role = roleRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val roleUser = userRepository.findFirstByRole(role)
        if (roleUser != null) {
            referencedWarning.key = "role.user.role.referenced"
            referencedWarning.addParam(roleUser.id)
            return referencedWarning
        }
        return null
    }

}
