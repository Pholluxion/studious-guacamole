package com.gopark.core.role

import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role

    fun existsByNameIgnoreCase(name: String?): Boolean

}
