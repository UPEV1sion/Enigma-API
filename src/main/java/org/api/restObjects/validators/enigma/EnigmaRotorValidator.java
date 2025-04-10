package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaRotors;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnigmaRotorValidator implements ConstraintValidator<ValidEnigmaRotors, String[]> {
    private static final List<String> ALLOWED_ROTORS = IntStream.rangeClosed(1, 8).mapToObj(String::valueOf).toList();

    @Override
    public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(strings).allMatch(s -> Objects.nonNull(s) && ALLOWED_ROTORS.contains(s))
                && Arrays.stream(strings).collect(Collectors.toSet()).size() == strings.length;
    }
}
