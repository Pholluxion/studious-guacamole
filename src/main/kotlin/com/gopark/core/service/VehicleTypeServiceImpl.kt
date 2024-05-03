package com.gopark.core.service

import com.gopark.core.domain.VehicleType
import com.gopark.core.dto.VehicleTypeDTO
import com.gopark.core.repos.SpotRepository
import com.gopark.core.repos.VehicleTypeRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class VehicleTypeServiceImpl(
    private val vehicleTypeRepository: VehicleTypeRepository,
    private val spotRepository: SpotRepository
) : VehicleTypeService {

    override fun findAll(): List<VehicleTypeDTO> {
        val vehicleTypes = vehicleTypeRepository.findAll(Sort.by("id"))
        return vehicleTypes.stream()
                .map { vehicleType -> mapToDTO(vehicleType, VehicleTypeDTO()) }
                .toList()
    }

    override fun get(id: Int): VehicleTypeDTO = vehicleTypeRepository.findById(id)
            .map { vehicleType -> mapToDTO(vehicleType, VehicleTypeDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(vehicleTypeDTO: VehicleTypeDTO): Int {
        val vehicleType = VehicleType()
        mapToEntity(vehicleTypeDTO, vehicleType)
        return vehicleTypeRepository.save(vehicleType).id!!
    }

    override fun update(id: Int, vehicleTypeDTO: VehicleTypeDTO) {
        val vehicleType = vehicleTypeRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(vehicleTypeDTO, vehicleType)
        vehicleTypeRepository.save(vehicleType)
    }

    override fun delete(id: Int) {
        vehicleTypeRepository.deleteById(id)
    }

    private fun mapToDTO(vehicleType: VehicleType, vehicleTypeDTO: VehicleTypeDTO): VehicleTypeDTO {
        vehicleTypeDTO.id = vehicleType.id
        vehicleTypeDTO.name = vehicleType.name
        vehicleTypeDTO.fee = vehicleType.fee
        return vehicleTypeDTO
    }

    private fun mapToEntity(vehicleTypeDTO: VehicleTypeDTO, vehicleType: VehicleType): VehicleType {
        vehicleType.name = vehicleTypeDTO.name
        vehicleType.fee = vehicleTypeDTO.fee
        return vehicleType
    }

    override fun nameExists(name: String?): Boolean =
            vehicleTypeRepository.existsByNameIgnoreCase(name)

    override fun getReferencedWarning(id: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val vehicleType = vehicleTypeRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val vehicleTypeSpot = spotRepository.findFirstByVehicleType(vehicleType)
        if (vehicleTypeSpot != null) {
            referencedWarning.key = "vehicleType.spot.vehicleType.referenced"
            referencedWarning.addParam(vehicleTypeSpot.id)
            return referencedWarning
        }
        return null
    }

}
