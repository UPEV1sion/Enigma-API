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
    String message() default "Invalid parameter: manual_keys";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
