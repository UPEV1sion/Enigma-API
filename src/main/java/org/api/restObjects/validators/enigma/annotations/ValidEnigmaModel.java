package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaModelValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaModelValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaModel {
    String message() default "Invalid enigma model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
