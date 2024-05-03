package com.gopark.core.dto

import com.gopark.core.model.PaymentStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.OffsetDateTime


class SpotDTO {

    var id: Int? = null

    @Size(max = 20)
    var licensePlate: String? = null

    var paymentStatus: PaymentStatus? = null

    var arrivalTime: OffsetDateTime? = null

    @NotNull
    var parking: Int? = null

    var vehicleType: Int? = null

}
