package com.gopark.core.repos

import com.gopark.core.domain.Payment
import com.gopark.core.domain.Spot
import org.springframework.data.jpa.repository.JpaRepository


interface PaymentRepository : JpaRepository<Payment, Int> {

    fun findFirstBySpot(spot: Spot): Payment?

}
