package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaReflector;

import java.util.List;

public class EnigmaReflectorValidator implements ConstraintValidator<ValidEnigmaReflector, Character> {
    private static final List<Character> ALLOWED_REFLECTORS = List.of('A', 'B', 'C', 'b', 'c');

    @Override
    public boolean isValid(Character c, ConstraintValidatorContext constraintValidatorContext) {
        return c != null && ALLOWED_REFLECTORS.contains(c);
    }
}
