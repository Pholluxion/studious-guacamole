package com.gopark.core.repos

import com.gopark.core.domain.Role
import com.gopark.core.domain.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(attributePaths = ["role"])
    fun findByEmailIgnoreCase(email: String): User?

    fun existsByEmailIgnoreCase(email: String?): Boolean

    fun findFirstByRole(role: Role): User?

    fun findByEmail(email: String): User?

}
