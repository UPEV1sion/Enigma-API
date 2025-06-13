package org.api.restObjects.validators.catalogue.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CataloguePageValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CataloguePageValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCataloguePage {
    String message() default "Must be a non-null integer between 0 and the last available page.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
