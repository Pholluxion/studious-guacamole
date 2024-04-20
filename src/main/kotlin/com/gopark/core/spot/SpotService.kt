package com.gopark.core.spot

import com.gopark.core.util.ReferencedWarning


interface SpotService {

    fun findAll(filter: String?): List<SpotDTO>

    fun get(spotId: Int): SpotDTO

    fun create(spotDTO: SpotDTO): Int

    fun update(spotId: Int, spotDTO: SpotDTO)

    fun delete(spotId: Int)

    fun getReferencedWarning(spotId: Int): ReferencedWarning?

}
