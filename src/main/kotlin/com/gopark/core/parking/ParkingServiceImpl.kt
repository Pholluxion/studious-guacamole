package com.gopark.core.parking

import com.gopark.core.spot.SpotRepository
import com.gopark.core.user.UserRepository
import com.gopark.core.util.NotFoundException
import com.gopark.core.util.ReferencedWarning
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class ParkingServiceImpl(
    private val parkingRepository: ParkingRepository,
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository
) : ParkingService {

    override fun findAll(filter: String?): List<ParkingDTO> {
        val parkings: List<Parking>
        val sort = Sort.by("parkingId")
        parkings = if (filter != null) {
            parkingRepository.findAllByParkingId(filter.toIntOrNull(), sort)
        } else {
            parkingRepository.findAll(sort)
        }
        return parkings.stream()
                .map { parking -> mapToDTO(parking, ParkingDTO()) }
                .toList()
    }

    override fun get(parkingId: Int): ParkingDTO = parkingRepository.findById(parkingId)
            .map { parking -> mapToDTO(parking, ParkingDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(parkingDTO: ParkingDTO): Int {
        val parking = Parking()
        mapToEntity(parkingDTO, parking)
        return parkingRepository.save(parking).parkingId!!
    }

    override fun update(parkingId: Int, parkingDTO: ParkingDTO) {
        val parking = parkingRepository.findById(parkingId)
                .orElseThrow { NotFoundException() }
        mapToEntity(parkingDTO, parking)
        parkingRepository.save(parking)
    }

    override fun delete(parkingId: Int) {
        parkingRepository.deleteById(parkingId)
    }

    private fun mapToDTO(parking: Parking, parkingDTO: ParkingDTO): ParkingDTO {
        parkingDTO.parkingId = parking.parkingId
        parkingDTO.location = parking.location
        parkingDTO.capacity = parking.capacity
        parkingDTO.owner = parking.owner?.id
        return parkingDTO
    }

    private fun mapToEntity(parkingDTO: ParkingDTO, parking: Parking): Parking {
        parking.location = parkingDTO.location
        parking.capacity = parkingDTO.capacity
        val owner = if (parkingDTO.owner == null) null else
                userRepository.findById(parkingDTO.owner!!)
                .orElseThrow { NotFoundException("owner not found") }
        parking.owner = owner
        return parking
    }

    override fun getReferencedWarning(parkingId: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val parking = parkingRepository.findById(parkingId)
                .orElseThrow { NotFoundException() }
        val parkingSpot = spotRepository.findFirstByParking(parking)
        if (parkingSpot != null) {
            referencedWarning.key = "parking.spot.parking.referenced"
            referencedWarning.addParam(parkingSpot.spotId)
            return referencedWarning
        }
        return null
    }

}
