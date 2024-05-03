package com.gopark.core.util

import com.gopark.core.annotation.EmailUnique
import com.gopark.core.service.RegistrationService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailValidator(
        private val registrationService: RegistrationService
) : ConstraintValidator<EmailUnique, String> {

    override fun isValid(value: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        return !registrationService.emailExists(value)
    }

}