package com.gopark.core.service

import com.gopark.core.dto.PaymentDTO


interface PaymentService {

    fun findAll(): List<PaymentDTO>

    fun get(id: Int): PaymentDTO

    fun create(paymentDTO: PaymentDTO): Int

    fun update(id: Int, paymentDTO: PaymentDTO)

    fun delete(id: Int)

}
