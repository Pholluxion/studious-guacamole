package com.gopark.core.payment

import com.gopark.core.config.BaseIT
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class PaymentResourceTest : BaseIT() {

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql",
        "/data/paymentData.sql"
    )
    fun getAllPayments_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.paymentDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.paymentDTOList[0].paymentId").value(1500))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/payments{?filter}"))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql",
        "/data/paymentData.sql"
    )
    fun getAllPayments_filtered() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments?filter=1501")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.paymentDTOList.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.paymentDTOList[0].paymentId").value(1501))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql",
        "/data/paymentData.sql"
    )
    fun getPayment_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments/1500")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.paidAmount").value(56.08))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/payments/1500"))
    }

    @Test
    fun getPayment_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments/2166")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql"
    )
    fun createPayment_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/paymentDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(1, paymentRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql",
        "/data/paymentData.sql"
    )
    fun updatePayment_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/payments/1500")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/paymentDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/payments/1500"))
        Assertions.assertEquals(BigDecimal("54.08"),
                paymentRepository.findById(1500).orElseThrow().paidAmount)
        Assertions.assertEquals(2, paymentRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/vehicleTypeData.sql",
        "/data/feeData.sql",
        "/data/paymentData.sql"
    )
    fun deletePayment_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/payments/1500")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, paymentRepository.count())
    }

}
