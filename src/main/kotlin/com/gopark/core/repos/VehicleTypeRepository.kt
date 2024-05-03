package com.gopark.core.repos

import com.gopark.core.domain.VehicleType
import org.springframework.data.jpa.repository.JpaRepository


interface VehicleTypeRepository : JpaRepository<VehicleType, Int> {

    fun existsByNameIgnoreCase(name: String?): Boolean

}
