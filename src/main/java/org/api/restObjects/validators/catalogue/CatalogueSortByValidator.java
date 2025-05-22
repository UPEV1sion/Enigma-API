package org.api.restObjects.validators.catalogue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.catalogue.annotations.ValidCatalogueSortBy;

import java.util.Set;

public class CatalogueSortByValidator implements ConstraintValidator<ValidCatalogueSortBy, String> {

    private static final Set<String> VALID_SORT_FIELDS = Set.of(
            "one_to_four_permut",
            "two_to_five_permut",
            "three_to_six_permut",
            "rotor_order",
            "rotor_position"
    );

    @Override
    public void initialize(ValidCatalogueSortBy constraintAnnotation) {
        // Initialization logic if necessary (not needed in this case)
    }

    @Override
    public boolean isValid(String sortBy, ConstraintValidatorContext context) {
        // If sortBy is null or blank, we consider it valid (because some cases may want unsorted data)
        return sortBy == null || sortBy.isBlank() || VALID_SORT_FIELDS.contains(sortBy);
    }
}