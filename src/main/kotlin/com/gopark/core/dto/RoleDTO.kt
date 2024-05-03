package com.gopark.core.dto

import com.gopark.core.annotation.RoleUnique
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RoleDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 10)
    @RoleUnique
    var name: String? = null

}
