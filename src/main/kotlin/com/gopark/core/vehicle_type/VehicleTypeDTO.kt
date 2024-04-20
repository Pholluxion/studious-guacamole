package com.gopark.core.vehicle_type

import jakarta.validation.constraints.Size


class VehicleTypeDTO {

    var vehicleTypeId: Int? = null

    @Size(max = 255)
    var description: String? = null

}
