package org.api.restObjects.validators.enigma.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.api.restObjects.validators.enigma.EnigmaPlugboardValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnigmaPlugboardValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnigmaPlugboard {
    String message() default "Must contain letters [A-Z] with an even count, maximum 26 letters, no separators allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
