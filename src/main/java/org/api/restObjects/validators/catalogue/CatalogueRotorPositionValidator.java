package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueRotorPosition;

public class CatalogueRotorPositionValidator implements ConstraintValidator<ValidCatalogueRotorPosition, Integer[]> {

    @Override
    public void initialize(ValidCatalogueRotorPosition constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Integer[] rotorPositions, ConstraintValidatorContext context) {
        if (rotorPositions == null || rotorPositions.length == 0) {
            return true;  // Null is considered valid (no filtering)
        }

        // Check if the array has exactly 3 elements (triplet)
        if (rotorPositions.length != 3) {
            return false;  // Must be a triplet
        }

        // Check that each value is between 0 and 25 (inclusive)
        for (Integer value : rotorPositions) {
            if (value < 0 || value > 25) {
                return false;  // Only values between 0 and 25 are valid (alphabet size)
            }
        }

        return true;
    }
}