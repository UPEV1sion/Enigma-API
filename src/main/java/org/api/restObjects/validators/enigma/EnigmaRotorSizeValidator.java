package org.api.restObjects.validators.enigma;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.enigma.Enigma;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaRotorSize;

public class EnigmaRotorSizeValidator implements ConstraintValidator<ValidEnigmaRotorSize, Enigma> {

    @Override
    public boolean isValid(Enigma enigma, ConstraintValidatorContext context) {
        if (enigma == null || enigma.positions() == null || enigma.model() == null || enigma.rotors() == null || enigma.rings() == null) {
            return true; // Let @NotNull or other annotations handle this
        }

        int expectedLength = switch (enigma.model()) {
            case 1, 2, 3 -> 3;
            case 4 -> 4;
            default -> -1;
        };

        if (expectedLength == -1) {
            return false;
        }

        return (enigma.positions().length == expectedLength && enigma.rings().length == expectedLength && enigma.rotors().length == expectedLength);
    }
}