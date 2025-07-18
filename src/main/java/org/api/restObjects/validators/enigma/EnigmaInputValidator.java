package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaInput;

public class EnigmaInputValidator implements ConstraintValidator<ValidEnigmaInput, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return s != null
                && s.length() <= 10001
                && s.matches("^[A-Za-z]*$");
    }
}
