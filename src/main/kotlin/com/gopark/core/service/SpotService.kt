package com.gopark.core.service

import com.gopark.core.dto.SpotDTO
import com.gopark.core.util.ReferencedWarning


interface SpotService {

    fun findAll(): List<SpotDTO>

    fun get(id: Int): SpotDTO

    fun getByLicensePlate(licensePlate: String): SpotDTO

    fun create(spotDTO: SpotDTO): Int

    fun update(id: Int, spotDTO: SpotDTO)

    fun delete(id: Int)

    fun findAllByParkingId(parkingId: Int): List<SpotDTO>

    fun getReferencedWarning(id: Int): ReferencedWarning?

}
