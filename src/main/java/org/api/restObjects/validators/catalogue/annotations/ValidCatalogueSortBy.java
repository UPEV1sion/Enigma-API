package org.api.restObjects.validators.catalogue.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CatalogueSortByValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CatalogueSortByValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatalogueSortBy {
    String message() default "Invalid sort field provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

