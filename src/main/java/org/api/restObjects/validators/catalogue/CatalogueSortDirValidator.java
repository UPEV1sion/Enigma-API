package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueSortDir;

public class CatalogueSortDirValidator implements ConstraintValidator<ValidCatalogueSortDir, String> {

    @Override
    public void initialize(ValidCatalogueSortDir constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sortDir, ConstraintValidatorContext context) {
        if (sortDir == null) {
            return false;
        }
        return sortDir.equalsIgnoreCase("asc") || sortDir.equalsIgnoreCase("desc");
    }
}
