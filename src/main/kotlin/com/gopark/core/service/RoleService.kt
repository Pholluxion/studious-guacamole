package com.gopark.core.service

import com.gopark.core.dto.RoleDTO
import com.gopark.core.util.ReferencedWarning


interface RoleService {

    fun findAll(): List<RoleDTO>

    fun get(id: Long): RoleDTO

    fun create(roleDTO: RoleDTO): Long

    fun update(id: Long, roleDTO: RoleDTO)

    fun delete(id: Long)

    fun nameExists(name: String?): Boolean

    fun getReferencedWarning(id: Long): ReferencedWarning?

}
