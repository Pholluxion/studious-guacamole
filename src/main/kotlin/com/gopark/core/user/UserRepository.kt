package com.gopark.core.user

import com.gopark.core.role.Role
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(attributePaths = ["role"])
    fun findByEmailIgnoreCase(email: String): User?

    fun findAllById(id: Long?, sort: Sort): List<User>

    fun existsByEmailIgnoreCase(email: String?): Boolean

    fun findFirstByRole(role: Role): User?

}
