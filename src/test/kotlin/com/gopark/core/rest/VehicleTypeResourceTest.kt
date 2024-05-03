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


class VehicleTypeResourceTest : BaseIT() {

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun getAllVehicleTypes_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/vehicleTypes")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1100))
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun getVehicleType_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/vehicleTypes/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Duis autem vel."))
    }

    @Test
    fun getVehicleType_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/vehicleTypes/1766")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun createVehicleType_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/vehicleTypeDTORequest.json"))
                .`when`()
                    .post("/api/vehicleTypes")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(1, vehicleTypeRepository.count())
    }

    @Test
    fun createVehicleType_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/vehicleTypeDTORequest_missingField.json"))
                .`when`()
                    .post("/api/vehicleTypes")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun updateVehicleType_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/vehicleTypeDTORequest.json"))
                .`when`()
                    .put("/api/vehicleTypes/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals("Nam liber tempor.",
                vehicleTypeRepository.findById(1100).orElseThrow().name)
        Assertions.assertEquals(2, vehicleTypeRepository.count())
    }

    @Test
    @Sql("/data/vehicleTypeData.sql")
    fun deleteVehicleType_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .delete("/api/vehicleTypes/1100")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
        Assertions.assertEquals(1, vehicleTypeRepository.count())
    }

}
