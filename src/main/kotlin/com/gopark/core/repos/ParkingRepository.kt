package com.gopark.core.repos

import com.gopark.core.domain.Parking
import org.springframework.data.jpa.repository.JpaRepository


interface ParkingRepository : JpaRepository<Parking, Int> {
}
