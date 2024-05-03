package com.gopark.core.dto

import com.gopark.core.annotation.VehicleTypeUnique
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class VehicleTypeDTO {

    var id: Int? = null

    @NotNull
    @Size(max = 255)
    @VehicleTypeUnique
    var name: String? = null

    @NotNull
    var fee: Long? = null

}
