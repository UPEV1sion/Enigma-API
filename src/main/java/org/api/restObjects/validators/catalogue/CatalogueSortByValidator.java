package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueSortBy;

import java.util.Set;

public class CatalogueSortByValidator implements ConstraintValidator<ValidCatalogueSortBy, String> {

    private static final Set<String> VALID_SORT_FIELDS = Set.of(
            "rotor_order",
            "rotor_position"
    );

    @Override
    public void initialize(ValidCatalogueSortBy constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sortBy, ConstraintValidatorContext context) {
        if (sortBy == null) {
            return false;
        }
        return VALID_SORT_FIELDS.contains(sortBy);
    }
}