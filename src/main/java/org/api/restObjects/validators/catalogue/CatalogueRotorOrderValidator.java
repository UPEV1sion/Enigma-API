package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueRotorOrder;

public class CatalogueRotorOrderValidator implements ConstraintValidator<ValidCatalogueRotorOrder, Integer[]> {

    @Override
    public void initialize(ValidCatalogueRotorOrder constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Integer[] rotorOrders, ConstraintValidatorContext context) {
        if (rotorOrders == null) {
            return false;
        }
        if (rotorOrders.length == 0) {
            return true;  // Null is considered valid (no filtering)
        }

        // Check if the array length is 3
        if (rotorOrders.length != 3) {
            return false;  // Array must have exactly 3 elements
        }

        boolean[] seen = new boolean[6]; // Index 1 to 5 used
        for (Integer value : rotorOrders) {
            if (value < 1 || value > 5) {
                return false; // Out of range
            }
            if (seen[value]) {
                return false; // Duplicate
            }
            seen[value] = true;
        }

        return true;
    }
}