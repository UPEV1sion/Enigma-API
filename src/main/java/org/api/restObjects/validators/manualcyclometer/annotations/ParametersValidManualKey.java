package org.api.restObjects.validators.manualcyclometer.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.manualcyclometer.ParametersManualKeyValidator;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = ParametersManualKeyValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersValidManualKey {
    String message() default "Must be an array of strings where each string is exactly 6 characters long, " +
            "contains only uppercase letters (A-Z), and consists of the first three letters repeated twice (e.g., 'ABCABC'). An empty array will be ignored.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
