package com.gopark.core.vehicle_type

import com.gopark.core.fee.FeeRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class VehicleTypeServiceImpl(
    private val vehicleTypeRepository: VehicleTypeRepository,
    private val feeRepository: FeeRepository
) : VehicleTypeService {

    override fun findAll(): List<VehicleTypeDTO> {
        val vehicleTypes = vehicleTypeRepository.findAll(Sort.by("vehicleTypeId"))
        return vehicleTypes.stream()
                .map { vehicleType -> mapToDTO(vehicleType, VehicleTypeDTO()) }
                .toList()
    }

    override fun get(vehicleTypeId: Int): VehicleTypeDTO =
            vehicleTypeRepository.findById(vehicleTypeId)
            .map { vehicleType -> mapToDTO(vehicleType, VehicleTypeDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(vehicleTypeDTO: VehicleTypeDTO): Int {
        val vehicleType = VehicleType()
        mapToEntity(vehicleTypeDTO, vehicleType)
        return vehicleTypeRepository.save(vehicleType).vehicleTypeId!!
    }

    override fun update(vehicleTypeId: Int, vehicleTypeDTO: VehicleTypeDTO) {
        val vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow { NotFoundException() }
        mapToEntity(vehicleTypeDTO, vehicleType)
        vehicleTypeRepository.save(vehicleType)
    }

    override fun delete(vehicleTypeId: Int) {
        vehicleTypeRepository.deleteById(vehicleTypeId)
    }

    private fun mapToDTO(vehicleType: VehicleType, vehicleTypeDTO: VehicleTypeDTO): VehicleTypeDTO {
        vehicleTypeDTO.vehicleTypeId = vehicleType.vehicleTypeId
        vehicleTypeDTO.description = vehicleType.description
        return vehicleTypeDTO
    }

    private fun mapToEntity(vehicleTypeDTO: VehicleTypeDTO, vehicleType: VehicleType): VehicleType {
        vehicleType.description = vehicleTypeDTO.description
        return vehicleType
    }

    override fun getReferencedWarning(vehicleTypeId: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow { NotFoundException() }
        val vehicleTypeFee = feeRepository.findFirstByVehicleType(vehicleType)
        if (vehicleTypeFee != null) {
            referencedWarning.key = "vehicleType.fee.vehicleType.referenced"
            referencedWarning.addParam(vehicleTypeFee.feeId)
            return referencedWarning
        }
        return null
    }

}
