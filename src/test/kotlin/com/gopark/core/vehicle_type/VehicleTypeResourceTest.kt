package com.gopark.core.vehicle_type

import com.gopark.core.config.BaseIT
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class VehicleTypeResourceTest : BaseIT() {

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun getAllVehicleTypes_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicleTypes")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.vehicleTypeDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.vehicleTypeDTOList[0].vehicleTypeId").value(1200))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/vehicleTypes"))
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun getVehicleType_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicleTypes/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.description").value("Duis autem vel."))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/vehicleTypes/1200"))
    }

    @Test
    fun getVehicleType_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicleTypes/1866")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.code").value("NOT_FOUND"))
    }

    @Test
    fun createVehicleType_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicleTypes")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/vehicleTypeDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(1, vehicleTypeRepository.count())
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun updateVehicleType_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/vehicleTypes/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/vehicleTypeDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/vehicleTypes/1200"))
        Assertions.assertEquals("Nam liber tempor.",
                vehicleTypeRepository.findById(1200).orElseThrow().description)
        Assertions.assertEquals(2, vehicleTypeRepository.count())
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun deleteVehicleType_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/vehicleTypes/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, vehicleTypeRepository.count())
    }

}
