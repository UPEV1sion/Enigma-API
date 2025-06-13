package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaRotorValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaRotorValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaRotors {
    String message() default "The first three 'rotors' must be numbered 1 to 8; if a fourth rotor is used, it must be 9 or 10.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
