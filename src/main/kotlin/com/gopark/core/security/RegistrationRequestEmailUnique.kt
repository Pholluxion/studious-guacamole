package com.gopark.core.security

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [RegistrationRequestEmailUniqueValidator::class])
annotation class RegistrationRequestEmailUnique(
    val message: String = "{registration.register.taken}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


