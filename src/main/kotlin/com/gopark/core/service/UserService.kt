package com.gopark.core.service

import com.gopark.core.dto.UserDTO
import com.gopark.core.util.ReferencedWarning


interface UserService {

    fun findAll(): List<UserDTO>

    fun get(id: Long): UserDTO

    fun create(userDTO: UserDTO): Long

    fun update(id: Long, userDTO: UserDTO)

    fun delete(id: Long)

    fun getReferencedWarning(id: Long): ReferencedWarning?

}
