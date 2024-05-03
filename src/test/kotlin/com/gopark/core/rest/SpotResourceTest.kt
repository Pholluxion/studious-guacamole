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


class SpotResourceTest : BaseIT() {

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun getAllSpots_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/spots")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1300))
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun getSpot_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/spots/1300")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("licensePlate", Matchers.equalTo("Lorem ipsum dolor."))
    }

    @Test
    fun getSpot_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/spots/1966")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    @Sql("/data/parkingData.sql")
    fun createSpot_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/spotDTORequest.json"))
                .`when`()
                    .post("/api/spots")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(1, spotRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun updateSpot_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/spotDTORequest.json"))
                .`when`()
                    .put("/api/spots/1300")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals("Ut wisi enim.",
                spotRepository.findById(1300).orElseThrow().licensePlate)
        Assertions.assertEquals(2, spotRepository.count())
    }

    @Test
    @Sql(
        "/data/parkingData.sql",
        "/data/spotData.sql"
    )
    fun deleteSpot_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .delete("/api/spots/1300")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
        Assertions.assertEquals(1, spotRepository.count())
    }

}
