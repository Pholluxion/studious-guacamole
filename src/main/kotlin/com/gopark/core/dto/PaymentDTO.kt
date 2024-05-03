package com.gopark.core.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.OffsetDateTime


class PaymentDTO {

    var id: Int? = null

    @Digits(
        integer = 12,
        fraction = 2
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(
        type = "string",
        example = "52.08"
    )
    var paidAmount: BigDecimal? = null

    var paymentTime: OffsetDateTime? = null

    @NotNull
    var spot: Int? = null

}
