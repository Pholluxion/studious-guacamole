package com.gopark.core.vehicle_type

import org.springframework.data.jpa.repository.JpaRepository


interface VehicleTypeRepository : JpaRepository<VehicleType, Int>
