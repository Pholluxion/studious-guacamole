package com.gopark.core.fee

import com.gopark.core.payment.PaymentRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import com.gopark.core.vehicle_type.VehicleTypeRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class FeeServiceImpl(
    private val feeRepository: FeeRepository,
    private val vehicleTypeRepository: VehicleTypeRepository,
    private val paymentRepository: PaymentRepository
) : FeeService {

    override fun findAll(): List<FeeDTO> {
        val fees = feeRepository.findAll(Sort.by("feeId"))
        return fees.stream()
                .map { fee -> mapToDTO(fee, FeeDTO()) }
                .toList()
    }

    override fun get(feeId: Int): FeeDTO = feeRepository.findById(feeId)
            .map { fee -> mapToDTO(fee, FeeDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(feeDTO: FeeDTO): Int {
        val fee = Fee()
        mapToEntity(feeDTO, fee)
        return feeRepository.save(fee).feeId!!
    }

    override fun update(feeId: Int, feeDTO: FeeDTO) {
        val fee = feeRepository.findById(feeId)
                .orElseThrow { NotFoundException() }
        mapToEntity(feeDTO, fee)
        feeRepository.save(fee)
    }

    override fun delete(feeId: Int) {
        feeRepository.deleteById(feeId)
    }

    private fun mapToDTO(fee: Fee, feeDTO: FeeDTO): FeeDTO {
        feeDTO.feeId = fee.feeId
        feeDTO.amount = fee.amount
        feeDTO.duration = fee.duration
        feeDTO.vehicleType = fee.vehicleType?.vehicleTypeId
        return feeDTO
    }

    private fun mapToEntity(feeDTO: FeeDTO, fee: Fee): Fee {
        fee.amount = feeDTO.amount
        fee.duration = feeDTO.duration
        val vehicleType = if (feeDTO.vehicleType == null) null else
                vehicleTypeRepository.findById(feeDTO.vehicleType!!)
                .orElseThrow { NotFoundException("vehicleType not found") }
        fee.vehicleType = vehicleType
        return fee
    }

    override fun getReferencedWarning(feeId: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val fee = feeRepository.findById(feeId)
                .orElseThrow { NotFoundException() }
        val feePayment = paymentRepository.findFirstByFee(fee)
        if (feePayment != null) {
            referencedWarning.key = "fee.payment.fee.referenced"
            referencedWarning.addParam(feePayment.paymentId)
            return referencedWarning
        }
        return null
    }

}
