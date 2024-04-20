package com.gopark.core.security

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class RegistrationRequestEmailUniqueValidator(
    private val registrationService: RegistrationService
) : ConstraintValidator<RegistrationRequestEmailUnique, String> {

    override fun isValid(value: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        return !registrationService.emailExists(value)
    }

}
