package com.gopark.core.parking

import com.gopark.core.config.BaseIT
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class ParkingResourceTest : BaseIT() {

    @Test
    @Sql("/data/parkingData.sql")
    fun getAllParkings_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkings")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.parkingDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.parkingDTOList[0].parkingId").value(1300))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/parkings{?filter}"))
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun getAllParkings_filtered() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkings?filter=1301")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.parkingDTOList.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.parkingDTOList[0].parkingId").value(1301))
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun getParking_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkings/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.location").value("Duis autem vel."))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/parkings/1300"))
    }

    @Test
    fun getParking_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkings/1966")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    fun createParking_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkings")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/parkingDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(1, parkingRepository.count())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun updateParking_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/parkings/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/parkingDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/parkings/1300"))
        Assertions.assertEquals("Nam liber tempor.",
                parkingRepository.findById(1300).orElseThrow().location)
        Assertions.assertEquals(2, parkingRepository.count())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun deleteParking_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/parkings/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, parkingRepository.count())
    }

}
