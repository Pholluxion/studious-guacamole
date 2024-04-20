package com.gopark.core.spot

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.OffsetDateTime


class SpotDTO {

    var spotId: Int? = null

    @Size(max = 20)
    var licensePlate: String? = null

    var paymentStatus: PaymentStatus? = null

    var arrivalTime: OffsetDateTime? = null

    @NotNull
    var parking: Int? = null

}
