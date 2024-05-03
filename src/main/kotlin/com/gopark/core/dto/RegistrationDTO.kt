package com.gopark.core.dto

import com.gopark.core.annotation.EmailUnique
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RegistrationDTO {

    @NotNull
    @Size(max = 255)
    var name: String? = null

    @NotNull
    @Size(max = 255)
    var surname: String? = null

    @NotNull
    @Size(max = 255)
    @EmailUnique
    var email: String? = null

    @NotNull
    @Size(max = 255)
    var password: String? = null

    @NotNull
    @Size(max = 100)
    var documentNumber: String? = null

}
