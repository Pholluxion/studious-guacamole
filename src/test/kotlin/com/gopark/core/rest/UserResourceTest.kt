package com.gopark.core.rest

import com.gopark.core.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus


class UserResourceTest : BaseIT() {

    @Test
    fun getAllUsers_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/users")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo(1000))
    }

    @Test
    fun getAllUsers_unauthorized() {
        RestAssured
                .given()
                    .redirects().follow(false)
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/users")
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .body("code", Matchers.equalTo("ACCESS_DENIED"))
    }

    @Test
    fun getUser_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/users/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Duis autem vel."))
    }

    @Test
    fun getUser_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .get("/api/users/1666")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"))
    }

    @Test
    fun createUser_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userDTORequest.json"))
                .`when`()
                    .post("/api/users")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
        Assertions.assertEquals(3, userRepository.count())
    }

    @Test
    fun createUser_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userDTORequest_missingField.json"))
                .`when`()
                    .post("/api/users")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("name"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"))
    }

    @Test
    fun updateUser_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userDTORequest.json"))
                .`when`()
                    .put("/api/users/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
        Assertions.assertEquals("Nam liber tempor.",
                userRepository.findById(1000).orElseThrow().name)
        Assertions.assertEquals(2, userRepository.count())
    }

    @Test
    fun deleteUser_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken())
                    .accept(ContentType.JSON)
                .`when`()
                    .delete("/api/users/1000")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
        Assertions.assertEquals(1, userRepository.count())
    }

}
