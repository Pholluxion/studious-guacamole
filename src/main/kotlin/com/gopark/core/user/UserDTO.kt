package com.gopark.core.user

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class UserDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 255)
    var name: String? = null

    @NotNull
    @Size(max = 255)
    var surname: String? = null

    @NotNull
    @Size(max = 255)
    var email: String? = null

    @NotNull
    @Size(max = 255)
    var password: String? = null

    @NotNull
    @Size(max = 100)
    var documentNumber: String? = null

    @NotNull
    var role: Long? = null

}
