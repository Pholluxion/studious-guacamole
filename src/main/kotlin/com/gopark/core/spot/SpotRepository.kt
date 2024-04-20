package com.gopark.core.spot

import com.gopark.core.parking.Parking
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository


interface SpotRepository : JpaRepository<Spot, Int> {

    fun findAllBySpotId(spotId: Int?, sort: Sort): List<Spot>

    fun findFirstByParking(parking: Parking): Spot?

}
