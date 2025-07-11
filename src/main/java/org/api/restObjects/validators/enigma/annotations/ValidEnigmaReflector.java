package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaReflectorValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaReflectorValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaReflector {
    String message() default "Only the values 'A', 'B', 'C', 'b', and 'c' are permitted.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
