package com.gopark.core.fee

import com.gopark.core.vehicle_type.VehicleType
import org.springframework.data.jpa.repository.JpaRepository


interface FeeRepository : JpaRepository<Fee, Int> {

    fun findFirstByVehicleType(vehicleType: VehicleType): Fee?

}
