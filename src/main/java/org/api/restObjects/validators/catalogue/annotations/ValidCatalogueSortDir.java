package org.api.restObjects.validators.catalogue.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CatalogueSortDirValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatalogueSortDirValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatalogueSortDir {
    String message() default "Must be either 'asc' or 'desc'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
