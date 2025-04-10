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
    String message() default "Invalid enigma rotors";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
