package com.gopark.core.service

import com.gopark.core.domain.Spot
import com.gopark.core.dto.SpotDTO
import com.gopark.core.repos.ParkingRepository
import com.gopark.core.repos.PaymentRepository
import com.gopark.core.repos.SpotRepository
import com.gopark.core.repos.VehicleTypeRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class SpotServiceImpl(
    private val spotRepository: SpotRepository,
    private val parkingRepository: ParkingRepository,
    private val vehicleTypeRepository: VehicleTypeRepository,
    private val paymentRepository: PaymentRepository
) : SpotService {

    override fun findAll(): List<SpotDTO> {
        val spots = spotRepository.findAll(Sort.by("id"))
        return spots.stream()
                .map { spot -> mapToDTO(spot, SpotDTO()) }
                .toList()
    }

    override fun get(id: Int): SpotDTO = spotRepository.findById(id)
            .map { spot -> mapToDTO(spot, SpotDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(spotDTO: SpotDTO): Int {
        val spot = Spot()
        mapToEntity(spotDTO, spot)
        return spotRepository.save(spot).id!!
    }

    override fun update(id: Int, spotDTO: SpotDTO) {
        val spot = spotRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(spotDTO, spot)
        spotRepository.save(spot)
    }

    override fun delete(id: Int) {
        spotRepository.deleteById(id)
    }

    override fun findAllByParkingId(parkingId: Int): List<SpotDTO> {
        val spots = spotRepository.findAllByParkingId(parkingId)
        return spots.stream()
                .map { spot -> mapToDTO(spot, SpotDTO()) }
                .toList()
    }

    private fun mapToDTO(spot: Spot, spotDTO: SpotDTO): SpotDTO {
        spotDTO.id = spot.id
        spotDTO.licensePlate = spot.licensePlate
        spotDTO.paymentStatus = spot.paymentStatus
        spotDTO.arrivalTime = spot.arrivalTime
        spotDTO.parking = spot.parking?.id
        spotDTO.vehicleType = spot.vehicleType?.id
        return spotDTO
    }

    private fun mapToEntity(spotDTO: SpotDTO, spot: Spot): Spot {
        spot.licensePlate = spotDTO.licensePlate
        spot.paymentStatus = spotDTO.paymentStatus
        spot.arrivalTime = spotDTO.arrivalTime
        val parking = if (spotDTO.parking == null) null else
                parkingRepository.findById(spotDTO.parking!!)
                .orElseThrow { NotFoundException("parking not found") }
        spot.parking = parking
        val vehicleType = if (spotDTO.vehicleType == null) null else
                vehicleTypeRepository.findById(spotDTO.vehicleType!!)
                .orElseThrow { NotFoundException("vehicleType not found") }
        spot.vehicleType = vehicleType
        return spot
    }

    override fun getReferencedWarning(id: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val spot = spotRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val spotPayment = paymentRepository.findFirstBySpot(spot)
        if (spotPayment != null) {
            referencedWarning.key = "spot.payment.spot.referenced"
            referencedWarning.addParam(spotPayment.id)
            return referencedWarning
        }
        return null
    }

}
