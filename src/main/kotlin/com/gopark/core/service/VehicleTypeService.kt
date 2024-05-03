package com.gopark.core.service

import com.gopark.core.dto.VehicleTypeDTO
import com.gopark.core.util.ReferencedWarning


interface VehicleTypeService {

    fun findAll(): List<VehicleTypeDTO>

    fun get(id: Int): VehicleTypeDTO

    fun create(vehicleTypeDTO: VehicleTypeDTO): Int

    fun update(id: Int, vehicleTypeDTO: VehicleTypeDTO)

    fun delete(id: Int)

    fun nameExists(name: String?): Boolean

    fun getReferencedWarning(id: Int): ReferencedWarning?

}
