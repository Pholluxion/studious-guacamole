package com.gopark.core.security

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    var email: String? = null

    @NotNull
    @Size(max = 255)
    var password: String? = null

}
