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
    String message() default "Must contain only letters [A-Z] or [a-z].";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
