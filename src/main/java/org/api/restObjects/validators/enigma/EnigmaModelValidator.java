package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaModel;

import java.util.List;

public class EnigmaModelValidator implements ConstraintValidator<ValidEnigmaModel, Integer> {
    private static final List<Integer> VALID_ENIGMA_MODELS = List.of(3, 4);

    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && VALID_ENIGMA_MODELS.contains(s);
    }
}
