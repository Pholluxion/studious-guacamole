package com.gopark.core.role

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RoleDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 10)
    @RoleNameUnique
    var name: String? = null

}
