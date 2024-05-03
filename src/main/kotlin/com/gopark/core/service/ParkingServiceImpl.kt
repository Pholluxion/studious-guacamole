package com.gopark.core.service

import com.gopark.core.domain.Parking
import com.gopark.core.dto.ParkingDTO
import com.gopark.core.repos.ParkingRepository
import com.gopark.core.repos.SpotRepository
import com.gopark.core.repos.UserRepository
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

    override fun findAll(): List<ParkingDTO> {
        val parkings = parkingRepository.findAll(Sort.by("id"))
        return parkings.stream()
                .map { parking -> mapToDTO(parking, ParkingDTO()) }
                .toList()
    }

    override fun get(id: Int): ParkingDTO = parkingRepository.findById(id)
            .map { parking -> mapToDTO(parking, ParkingDTO()) }
            .orElseThrow { NotFoundException() }

    override fun create(parkingDTO: ParkingDTO): Int {
        val parking = Parking()
        mapToEntity(parkingDTO, parking)
        return parkingRepository.save(parking).id!!
    }

    override fun update(id: Int, parkingDTO: ParkingDTO) {
        val parking = parkingRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(parkingDTO, parking)
        parkingRepository.save(parking)
    }

    override fun delete(id: Int) {
        parkingRepository.deleteById(id)
    }

    private fun mapToDTO(parking: Parking, parkingDTO: ParkingDTO): ParkingDTO {
        parkingDTO.id = parking.id
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

    override fun getReferencedWarning(id: Int): ReferencedWarning? {
        val referencedWarning = ReferencedWarning()
        val parking = parkingRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val parkingSpot = spotRepository.findFirstByParking(parking)
        if (parkingSpot != null) {
            referencedWarning.key = "parking.spot.parking.referenced"
            referencedWarning.addParam(parkingSpot.id)
            return referencedWarning
        }
        return null
    }

}
