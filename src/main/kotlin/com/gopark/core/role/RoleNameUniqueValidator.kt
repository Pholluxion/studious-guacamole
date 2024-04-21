package com.gopark.core.role

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.servlet.HandlerMapping

class RoleNameUniqueValidator(
    private val roleService: RoleService,
    private val request: HttpServletRequest
) : ConstraintValidator<RoleNameUnique, String> {

    override fun isValid(value: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        @Suppress("unchecked_cast") val pathVariables =
            (request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as
                    Map<String, String>)
        val currentId = pathVariables["id"]
        if (currentId != null && value.equals(roleService.get(currentId.toLong()).name, ignoreCase =
            true)) {
            // value hasn't changed
            return true
        }
        return !roleService.nameExists(value)
    }

}
