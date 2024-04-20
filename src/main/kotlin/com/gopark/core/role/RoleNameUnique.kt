package com.gopark.core.role

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass



@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [RoleNameUniqueValidator::class])
annotation class RoleNameUnique(
    val message: String = "{Exists.role.name}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


