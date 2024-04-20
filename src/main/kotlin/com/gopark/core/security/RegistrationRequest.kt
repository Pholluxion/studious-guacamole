package com.gopark.core.security

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    var name: String? = null

    @NotNull
    @Size(max = 255)
    var surname: String? = null

    @NotNull
    @Size(max = 255)
    @RegistrationRequestEmailUnique
    var email: String? = null

    @NotNull
    @Size(max = 255)
    var password: String? = null

    @NotNull
    @Size(max = 100)
    var documentNumber: String? = null

}
