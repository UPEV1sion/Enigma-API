package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaPlugboard;

import java.util.stream.Collectors;

public class EnigmaPlugboardValidator implements ConstraintValidator<ValidEnigmaPlugboard, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null
                && s.matches("^[A-Z]*$")
                && s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()).size() == s.length();
    }
}
