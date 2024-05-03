package com.gopark.core.service

import com.gopark.core.dto.ParkingDTO
import com.gopark.core.util.ReferencedWarning


interface ParkingService {

    fun findAll(): List<ParkingDTO>

    fun get(id: Int): ParkingDTO

    fun create(parkingDTO: ParkingDTO): Int

    fun update(id: Int, parkingDTO: ParkingDTO)

    fun delete(id: Int)

    fun getReferencedWarning(id: Int): ReferencedWarning?

}
