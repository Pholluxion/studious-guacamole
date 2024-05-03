package com.gopark.core.annotation


import com.gopark.core.util.EmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [EmailValidator::class])
annotation class EmailUnique(
        val message: String = "{registration.register.taken}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

