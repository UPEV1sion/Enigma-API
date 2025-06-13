package org.api.restObjects.validators.catalogue.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.catalogue.CyclometerCycleValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CyclometerCycleValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCyclometerCycle {
    String message() default "Must be a non-null array that is either empty or contains integers from 1 to 13, with the total sum not exceeding 13.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
