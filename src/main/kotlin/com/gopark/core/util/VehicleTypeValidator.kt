package com.gopark.core.util

import com.gopark.core.annotation.VehicleTypeUnique
import com.gopark.core.service.VehicleTypeService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.servlet.HandlerMapping


class VehicleTypeValidator(
        private val vehicleTypeService: VehicleTypeService,
        private val request: HttpServletRequest
) : ConstraintValidator<VehicleTypeUnique, String> {

    override fun isValid(`value`: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        @Suppress("unchecked_cast") val pathVariables =
                (request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as
                        Map<String, String>)
        val currentId = pathVariables["id"]
        if (currentId != null && value.equals(vehicleTypeService.get(currentId.toInt()).name,
                        ignoreCase = true)) {
            return true
        }
        return !vehicleTypeService.nameExists(value)
    }

}