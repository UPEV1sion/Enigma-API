package org.api.restObjects.validators.cyclometer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.cyclometer.annotations.ParametersValidDailyKeyCount;

public class ParametersDailyKeyCountValidator implements ConstraintValidator<ParametersValidDailyKeyCount, Integer> {
    private static final int MAX_DAILY_KEY_COUNT = 1024;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 0 && value <= MAX_DAILY_KEY_COUNT;
    }
}
