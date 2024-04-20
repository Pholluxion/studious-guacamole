package com.gopark.core.security

import com.gopark.core.config.BaseIT
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class RegistrationResourceTest : BaseIT() {

    @Test
    fun register_success() {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/registrationRequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
        Assertions.assertEquals(3, userRepository.count())
    }

}
