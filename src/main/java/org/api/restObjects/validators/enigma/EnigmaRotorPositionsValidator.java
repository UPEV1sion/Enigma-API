package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaRotorPositions;

import java.util.Arrays;

public class EnigmaRotorPositionsValidator implements ConstraintValidator<ValidEnigmaRotorPositions, Integer[]> {

    @Override
    public boolean isValid(Integer[] integers, ConstraintValidatorContext context) {
        // Ensure the array is not null and all values are between 0 and 25 (inclusive)
        return integers != null &&
                Arrays.stream(integers)
                        .allMatch(i -> i != null && i >= 0 && i <= 25);
    }
}