package com.gopark.core.rest

import com.gopark.core.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus


class RoleResourceTest : BaseIT() {

    @Test
    fun getAllRoles_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/roles")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1500))
    }

    @Test
    fun getRole_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/roles/1500")
                .then()
                    .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun getRole_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/roles/2166")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"))
    }

    @Test
    fun createRole_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/roleDTORequest.json"))
                .`when`()
                    .post("/api/roles")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(3, roleRepository.count())
    }

    @Test
    fun createRole_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/roleDTORequest_missingField.json"))
                .`when`()
                    .post("/api/roles")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("name"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"))
    }

    @Test
    fun updateRole_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/roleDTORequest.json"))
                .`when`()
                    .put("/api/roles/1500")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals(2, roleRepository.count())
    }

}
