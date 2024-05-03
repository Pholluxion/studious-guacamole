package com.gopark.core.repos

import com.gopark.core.domain.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role

    fun existsByNameIgnoreCase(name: String?): Boolean

}
