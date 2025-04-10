package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaInputValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaInputValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaInput {
    String message() default "Invalid enigma input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
