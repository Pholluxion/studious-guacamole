package com.gopark.core.repos

import com.gopark.core.domain.Parking
import com.gopark.core.domain.Spot
import com.gopark.core.domain.VehicleType
import org.springframework.data.jpa.repository.JpaRepository


interface SpotRepository : JpaRepository<Spot, Int> {

    fun findFirstByParking(parking: Parking): Spot?

    fun findFirstByVehicleType(vehicleType: VehicleType): Spot?

}
