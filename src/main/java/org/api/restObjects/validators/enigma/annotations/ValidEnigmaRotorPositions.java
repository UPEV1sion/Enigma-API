package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaRotorPositionsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaRotorPositionsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaRotorPositions {
    String message() default "Invalid enigma rotor positions";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
