package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaRotorPositions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class EnigmaRotorPositionsValidator implements ConstraintValidator<ValidEnigmaRotorPositions, String[]> {
    private static final List<String> ALLOWED_ROTOR_POSITIONS = IntStream.range(0, 26).mapToObj(String::valueOf).toList();

    @Override
    public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
        return strings != null && Arrays.stream(strings).allMatch(s -> Objects.nonNull(s) && ALLOWED_ROTOR_POSITIONS.contains(s));
    }
}
