package com.gopark.core.vehicle_type

import com.gopark.core.util.ReferencedWarning


interface VehicleTypeService {

    fun findAll(): List<VehicleTypeDTO>

    fun get(vehicleTypeId: Int): VehicleTypeDTO

    fun create(vehicleTypeDTO: VehicleTypeDTO): Int

    fun update(vehicleTypeId: Int, vehicleTypeDTO: VehicleTypeDTO)

    fun delete(vehicleTypeId: Int)

    fun getReferencedWarning(vehicleTypeId: Int): ReferencedWarning?

}
