package com.gopark.core.parking

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class ParkingDTO {

    var parkingId: Int? = null

    @Size(max = 255)
    var location: String? = null

    var capacity: Int? = null

    @NotNull
    var owner: Long? = null

}
