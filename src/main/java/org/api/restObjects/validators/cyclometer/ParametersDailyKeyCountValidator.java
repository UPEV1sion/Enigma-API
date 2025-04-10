package org.api.restObjects.validators.cyclometer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.cyclometer.annotations.ParametersValidDailyKeyCount;

public class ParametersDailyKeyCountValidator implements ConstraintValidator<ParametersValidDailyKeyCount, String> {
    private static final int MAX_DAILY_KEY_COUNT = 1024;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches("^[0-9]+$") && Integer.parseInt(s) <= MAX_DAILY_KEY_COUNT;
    }
}
