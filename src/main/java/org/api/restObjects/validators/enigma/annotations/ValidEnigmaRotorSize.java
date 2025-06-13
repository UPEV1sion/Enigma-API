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
    String message() default "For 'model' 3, the arrays 'rotors', 'positions', and 'rings' must each contain exactly 3 elements. For Model 4, these arrays must contain exactly 4 elements.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
