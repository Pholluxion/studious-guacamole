package com.gopark.core.rest

import com.gopark.core.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


class ParkingResourceTest : BaseIT() {

    @Test
    @Sql("/data/parkingData.sql")
    fun getAllParkings_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/parkings")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1200))
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun getParking_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/parkings/1200")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("location", Matchers.equalTo("Duis autem vel."))
    }

    @Test
    fun getParking_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/parkings/1866")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())

    }

    @Test
    fun createParking_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/parkingDTORequest.json"))
                .`when`()
                    .post("/api/parkings")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(1, parkingRepository.count())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun updateParking_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/parkingDTORequest.json"))
                .`when`()
                    .put("/api/parkings/1200")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals("Nam liber tempor.",
                parkingRepository.findById(1200).orElseThrow().location)
        Assertions.assertEquals(2, parkingRepository.count())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun deleteParking_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .delete("/api/parkings/1200")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
        Assertions.assertEquals(1, parkingRepository.count())
    }

}
