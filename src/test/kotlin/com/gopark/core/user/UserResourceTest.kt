package com.gopark.core.user

import com.gopark.core.config.BaseIT
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class UserResourceTest : BaseIT() {

    @Test
    fun getAllUsers_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.userDTOList.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.userDTOList[0].id").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/users{?filter}"))
    }

    @Test
    fun getAllUsers_filtered() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?filter=1001")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.userDTOList.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._embedded.userDTOList[0].id").value(1001))
    }

    @Test
    fun getAllUsers_unauthorized() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
    }

    @Test
    fun getUser_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value("Duis autem vel."))
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/users/1000"))
    }

    @Test
    fun getUser_notFound() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1666")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    fun createUser_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/userDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
        Assertions.assertEquals(3, userRepository.count())
    }

    @Test
    fun createUser_missingField() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/userDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    @Test
    fun updateUser_success() {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/userDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$._links.self.href").value("http://localhost/api/users/1000"))
        Assertions.assertEquals("Nam liber tempor.",
                userRepository.findById(1000).orElseThrow().name)
        Assertions.assertEquals(2, userRepository.count())
    }

    @Test
    fun deleteUser_success() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        Assertions.assertEquals(1, userRepository.count())
    }

}
