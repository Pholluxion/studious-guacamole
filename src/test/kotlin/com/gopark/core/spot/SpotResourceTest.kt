package com.gopark.core.spot

import com.gopark.core.config.BaseIT
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class SpotResourceTest : BaseIT() {

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun getAllSpots_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/spots")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.spotDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.spotDTOList[0].spotId").value(1400))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/spots{?filter}"))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun getAllSpots_filtered() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/spots?filter=1401")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.spotDTOList.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.spotDTOList[0].spotId").value(1401))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun getSpot_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/spots/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.licensePlate").value("Lorem ipsum dolor."))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/spots/1400"))
    }

    @Test
    fun getSpot_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/spots/2066")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun createSpot_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/spots")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/spotDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(1, spotRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun updateSpot_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/spots/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/spotDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/spots/1400"))
        Assertions.assertEquals("Ut wisi enim.",
                spotRepository.findById(1400).orElseThrow().licensePlate)
        Assertions.assertEquals(2, spotRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun deleteSpot_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/spots/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, spotRepository.count())
    }

}
