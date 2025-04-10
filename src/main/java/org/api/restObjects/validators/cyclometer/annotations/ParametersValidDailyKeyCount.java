package org.api.restObjects.validators.cyclometer.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.cyclometer.ParametersDailyKeyCountValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ParametersDailyKeyCountValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersValidDailyKeyCount {
    String message() default "Invalid parameter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
