package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCyclometerCycle;

import java.util.Arrays;

public class CyclometerCycleValidator implements ConstraintValidator<ValidCyclometerCycle, int[]> {
    private static final int MAX_CYCLE_SUM = 13;

    @Override
    public boolean isValid(int[] intArray, ConstraintValidatorContext constraintValidatorContext) {
        return intArray != null
                && Arrays.stream(intArray).allMatch(i -> i <= MAX_CYCLE_SUM && i > 0)
                && Arrays.stream(intArray).sum() <= 13;
    }
}
