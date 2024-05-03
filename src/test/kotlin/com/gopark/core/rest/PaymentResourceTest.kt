package com.gopark.core.rest

import com.gopark.core.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import java.math.BigDecimal
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


class PaymentResourceTest : BaseIT() {

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/paymentData.sql"
    )
    fun getAllPayments_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/payments")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1400))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/paymentData.sql"
    )
    fun getPayment_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/payments/1400")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("paidAmount", Matchers.equalTo(56.08))
    }

    @Test
    fun getPayment_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/payments/2066")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun createPayment_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/paymentDTORequest.json"))
                .`when`()
                    .post("/api/payments")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(1, paymentRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/paymentData.sql"
    )
    fun updatePayment_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/paymentDTORequest.json"))
                .`when`()
                    .put("/api/payments/1400")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals(BigDecimal("54.08"),
                paymentRepository.findById(1400).orElseThrow().paidAmount)
        Assertions.assertEquals(2, paymentRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql",
        "/data/paymentData.sql"
    )
    fun deletePayment_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .delete("/api/payments/1400")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
        Assertions.assertEquals(1, paymentRepository.count())
    }

}
