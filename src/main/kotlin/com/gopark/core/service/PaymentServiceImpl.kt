package com.gopark.core.service

import com.gopark.core.domain.Payment
import com.gopark.core.dto.PaymentDTO
import com.gopark.core.repos.PaymentRepository
import com.gopark.core.repos.SpotRepository
import com.gopark.core.util.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val spotRepository: SpotRepository
) : PaymentService {

    override fun findAll(): List<PaymentDTO> {
        val payments = paymentRepository.findAll(Sort.by("id"))
        return payments.stream()
                .map { payment -> mapToDTO(payment, PaymentDTO()) }
                .toList()
    }

    override fun get(id: Int): PaymentDTO = paymentRepository.findById(id)
            .map { payment -> mapToDTO(payment, PaymentDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(paymentDTO: PaymentDTO): Int {
        val payment = Payment()
        mapToEntity(paymentDTO, payment)
        return paymentRepository.save(payment).id!!
    }

    override fun update(id: Int, paymentDTO: PaymentDTO) {
        val payment = paymentRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(paymentDTO, payment)
        paymentRepository.save(payment)
    }

    override fun delete(id: Int) {
        paymentRepository.deleteById(id)
    }

    private fun mapToDTO(payment: Payment, paymentDTO: PaymentDTO): PaymentDTO {
        paymentDTO.id = payment.id
        paymentDTO.paidAmount = payment.paidAmount
        paymentDTO.paymentTime = payment.paymentTime
        paymentDTO.spot = payment.spot?.id
        return paymentDTO
    }

    private fun mapToEntity(paymentDTO: PaymentDTO, payment: Payment): Payment {
        payment.paidAmount = paymentDTO.paidAmount
        payment.paymentTime = paymentDTO.paymentTime
        val spot = if (paymentDTO.spot == null) null else spotRepository.findById(paymentDTO.spot!!)
                .orElseThrow { NotFoundException("spot not found") }
        payment.spot = spot
        return payment
    }

}
