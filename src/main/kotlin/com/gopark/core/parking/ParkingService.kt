package com.gopark.core.parking

import com.gopark.core.util.ReferencedWarning


interface ParkingService {

    fun findAll(filter: String?): List<ParkingDTO>

    fun get(parkingId: Int): ParkingDTO

    fun create(parkingDTO: ParkingDTO): Int

    fun update(parkingId: Int, parkingDTO: ParkingDTO)

    fun delete(parkingId: Int)

    fun getReferencedWarning(parkingId: Int): ReferencedWarning?

}
