package org.api.restObjects.validators.catalogue.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CatalogueRotorOrderValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatalogueRotorOrderValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatalogueRotorOrder {
    String message() default "Must be either an empty array or an array of 3 unique integers, each between 1 and 5.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}