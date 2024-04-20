package com.gopark.core.parking

import com.gopark.core.user.User
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository


interface ParkingRepository : JpaRepository<Parking, Int> {

    fun findAllByParkingId(parkingId: Int?, sort: Sort): List<Parking>

    fun findFirstByOwner(user: User): Parking?

}
