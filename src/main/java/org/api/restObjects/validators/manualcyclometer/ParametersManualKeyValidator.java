package org.api.restObjects.validators.manualcyclometer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.manualcyclometer.annotations.ParametersValidManualKey;


public class ParametersManualKeyValidator implements ConstraintValidator<ParametersValidManualKey, String[]> {
    private static final int MAX_DAILY_KEY_COUNT = 1024;

    @Override
    public boolean isValid(String[] arr, ConstraintValidatorContext constraintValidatorContext) {
        if (arr == null) return false;
        if (arr.length == 0 ) return true;
        if (arr.length > MAX_DAILY_KEY_COUNT) return false;

        for (String s : arr) {
            if (s == null || !s.matches("^([A-Z])([A-Z])([A-Z])\\1\\2\\3$")) return false;
        }

        return true;
    }
}
