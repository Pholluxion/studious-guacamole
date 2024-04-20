package com.gopark.core.payment

import com.gopark.core.fee.FeeRepository
import com.gopark.core.spot.SpotRepository
import com.gopark.core.util.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val spotRepository: SpotRepository,
    private val feeRepository: FeeRepository
) : PaymentService {

    override fun findAll(filter: String?): List<PaymentDTO> {
        var payments: List<Payment>
        val sort = Sort.by("paymentId")
        if (filter != null) {
            payments = paymentRepository.findAllByPaymentId(filter.toIntOrNull(), sort)
        } else {
            payments = paymentRepository.findAll(sort)
        }
        return payments.stream()
                .map { payment -> mapToDTO(payment, PaymentDTO()) }
                .toList()
    }

    override fun `get`(paymentId: Int): PaymentDTO = paymentRepository.findById(paymentId)
            .map { payment -> mapToDTO(payment, PaymentDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(paymentDTO: PaymentDTO): Int {
        val payment = Payment()
        mapToEntity(paymentDTO, payment)
        return paymentRepository.save(payment).paymentId!!
    }

    override fun update(paymentId: Int, paymentDTO: PaymentDTO) {
        val payment = paymentRepository.findById(paymentId)
                .orElseThrow { NotFoundException() }
        mapToEntity(paymentDTO, payment)
        paymentRepository.save(payment)
    }

    override fun delete(paymentId: Int) {
        paymentRepository.deleteById(paymentId)
    }

    private fun mapToDTO(payment: Payment, paymentDTO: PaymentDTO): PaymentDTO {
        paymentDTO.paymentId = payment.paymentId
        paymentDTO.paidAmount = payment.paidAmount
        paymentDTO.paymentTime = payment.paymentTime
        paymentDTO.spot = payment.spot?.spotId
        paymentDTO.fee = payment.fee?.feeId
        return paymentDTO
    }

    private fun mapToEntity(paymentDTO: PaymentDTO, payment: Payment): Payment {
        payment.paidAmount = paymentDTO.paidAmount
        payment.paymentTime = paymentDTO.paymentTime
        val spot = if (paymentDTO.spot == null) null else spotRepository.findById(paymentDTO.spot!!)
                .orElseThrow { NotFoundException("spot not found") }
        payment.spot = spot
        val fee = if (paymentDTO.fee == null) null else feeRepository.findById(paymentDTO.fee!!)
                .orElseThrow { NotFoundException("fee not found") }
        payment.fee = fee
        return payment
    }

}
