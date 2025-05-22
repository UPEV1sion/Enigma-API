package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaRotorSizeValidator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnigmaRotorSizeValidator.class)
public @interface ValidEnigmaRotorSize {
    String message() default "Rotor positions must match the expected size for the Enigma model";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
