package com.gopark.core.spot

import com.gopark.core.parking.ParkingRepository
import com.gopark.core.payment.PaymentRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class SpotServiceImpl(
    private val spotRepository: SpotRepository,
    private val parkingRepository: ParkingRepository,
    private val paymentRepository: PaymentRepository
) : SpotService {

    override fun findAll(filter: String?): List<SpotDTO> {
        val spots: List<Spot>
        val sort = Sort.by("spotId")
        spots = if (filter != null) {
            spotRepository.findAllBySpotId(filter.toIntOrNull(), sort)
        } else {
            spotRepository.findAll(sort)
        }
        return spots.stream()
                .map { spot -> mapToDTO(spot, SpotDTO()) }
                .toList()
    }

    override fun get(spotId: Int): SpotDTO = spotRepository.findById(spotId)
            .map { spot -> mapToDTO(spot, SpotDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(spotDTO: SpotDTO): Int {
        val spot = Spot()
        mapToEntity(spotDTO, spot)
        return spotRepository.save(spot).spotId!!
    }

    override fun update(spotId: Int, spotDTO: SpotDTO) {
        val spot = spotRepository.findById(spotId)
                .orElseThrow { NotFoundException() }
        mapToEntity(spotDTO, spot)
        spotRepository.save(spot)
    }

    override fun delete(spotId: Int) {
        spotRepository.deleteById(spotId)
    }

    private fun mapToDTO(spot: Spot, spotDTO: SpotDTO): SpotDTO {
        spotDTO.spotId = spot.spotId
        spotDTO.licensePlate = spot.licensePlate
        spotDTO.paymentStatus = spot.paymentStatus
        spotDTO.arrivalTime = spot.arrivalTime
        spotDTO.parking = spot.parking?.parkingId
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
        return spot
    }

    override fun getReferencedWarning(spotId: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val spot = spotRepository.findById(spotId)
                .orElseThrow { NotFoundException() }
        val spotPayment = paymentRepository.findFirstBySpot(spot)
        if (spotPayment != null) {
            referencedWarning.key = "spot.payment.spot.referenced"
            referencedWarning.addParam(spotPayment.paymentId)
            return referencedWarning
        }
        return null
    }

}
