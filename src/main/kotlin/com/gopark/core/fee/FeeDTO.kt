package com.gopark.core.fee

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal


class FeeDTO {

    var feeId: Int? = null

    @Digits(
        integer = 12,
        fraction = 2
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(
        type = "string",
        example = "92.08"
    )
    var amount: BigDecimal? = null

    @Size(max = 50)
    var duration: String? = null

    @NotNull
    var vehicleType: Int? = null

}
