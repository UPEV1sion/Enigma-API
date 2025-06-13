package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCataloguePage;
import org.db.DatabaseConfig;

public class CataloguePageValidator implements ConstraintValidator<ValidCataloguePage, Integer> {
    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {
        if (i == null) {
            return false;
        }
        return i < DatabaseConfig.NUM_PAGES && i >= 0;
    }
}
