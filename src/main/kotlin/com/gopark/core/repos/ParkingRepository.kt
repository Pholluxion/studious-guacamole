package com.gopark.core.repos

import com.gopark.core.domain.Parking
import com.gopark.core.domain.User
import org.springframework.data.jpa.repository.JpaRepository


interface ParkingRepository : JpaRepository<Parking, Int> {

    fun findFirstByOwner(user: User): Parking?

}
