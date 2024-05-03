package com.gopark.core.util

import com.gopark.core.annotation.RoleUnique
import com.gopark.core.service.RoleService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.servlet.HandlerMapping

class RoleValidator(
        private val roleService: RoleService,
        private val request: HttpServletRequest
) : ConstraintValidator<RoleUnique, String> {

    override fun isValid(`value`: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        @Suppress("unchecked_cast") val pathVariables =
                (request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as
                        Map<String, String>)
        val currentId = pathVariables["id"]
        if (currentId != null && value.equals(roleService.get(currentId.toLong()).name, ignoreCase =
                true)) {
            return true
        }
        return !roleService.nameExists(value)
    }

}