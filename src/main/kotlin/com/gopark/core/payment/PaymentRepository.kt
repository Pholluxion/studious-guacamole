package com.gopark.core.payment

import com.gopark.core.fee.Fee
import com.gopark.core.spot.Spot
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository


interface PaymentRepository : JpaRepository<Payment, Int> {

    fun findAllByPaymentId(paymentId: Int?, sort: Sort): List<Payment>

    fun findFirstBySpot(spot: Spot): Payment?

    fun findFirstByFee(fee: Fee): Payment?

}
