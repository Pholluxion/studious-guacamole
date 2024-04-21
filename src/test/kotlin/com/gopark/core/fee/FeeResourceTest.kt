package com.gopark.core.fee

import com.gopark.core.config.BaseIT
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class FeeResourceTest : BaseIT() {

    @Test
    @Sql(
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql"
    )
    fun getAllFees_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fees")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.feeDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.feeDTOList[0].feeId").value(1100))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/fees"))
    }

    @Test
    @Sql(
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql"
    )
    fun getFee_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fees/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.amount").value(96.08))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/fees/1100"))
    }

    @Test
    fun getFee_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fees/1766")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun createFee_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/fees")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/feeDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(1, feeRepository.count())
    }

    @Test
    @Sql(
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql"
    )
    fun updateFee_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/fees/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/feeDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/fees/1100"))
        Assertions.assertEquals(BigDecimal("94.08"),
                feeRepository.findById(1100).orElseThrow().amount)
        Assertions.assertEquals(2, feeRepository.count())
    }

    @Test
    @Sql(
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql"
    )
    fun deleteFee_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/fees/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, feeRepository.count())
    }

}
