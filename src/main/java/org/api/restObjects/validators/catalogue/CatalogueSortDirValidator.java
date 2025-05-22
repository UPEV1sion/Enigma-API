package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueSortDir;

public class CatalogueSortDirValidator implements ConstraintValidator<ValidCatalogueSortDir, String> {

    @Override
    public void initialize(ValidCatalogueSortDir constraintAnnotation) {
        // Initialization logic if necessary (not needed in this case)
    }

    @Override
    public boolean isValid(String sortDir, ConstraintValidatorContext context) {
        // If sortDir is null or blank, we consider it valid as well (you can handle defaults elsewhere)
        return sortDir == null || sortDir.isBlank() || sortDir.equalsIgnoreCase("asc") || sortDir.equalsIgnoreCase("desc");
    }
}
