package org.api.restObjects.validators.catalogue.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CatalogueRotorPositionValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatalogueRotorPositionValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatalogueRotorPosition {
    String message() default "Invalid rotor position provided: must be either an empty array or an array of 3 integers, each between 0 and 25.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}