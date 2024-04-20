package com.gopark.core.payment


interface PaymentService {

    fun findAll(filter: String?): List<PaymentDTO>

    fun get(paymentId: Int): PaymentDTO

    fun create(paymentDTO: PaymentDTO): Int

    fun update(paymentId: Int, paymentDTO: PaymentDTO)

    fun delete(paymentId: Int)

}
