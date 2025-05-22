package org.api.restObjects.validators.enigma;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.api.restObjects.validators.enigma.annotations.ValidEnigmaRotors;

import java.util.List;
import java.util.stream.IntStream;

public class EnigmaRotorValidator implements ConstraintValidator<ValidEnigmaRotors, Integer[]> {

    private static final List<Integer> ALLOWED_STANDARD_ROTORS = IntStream.rangeClosed(1, 8).boxed().toList();
    private static final List<Integer> ALLOWED_REFLECTORS = IntStream.rangeClosed(9, 10).boxed().toList();

    @Override
    public boolean isValid(Integer[] rotors, ConstraintValidatorContext context) {
        if (rotors == null || (rotors.length != 3 && rotors.length != 4)) {
            return false;
        }

        // Prüfe auf Null-Werte
        for (Integer rotor : rotors) {
            if (rotor == null) {
                return false;
            }
        }

        // Prüfe Index 0–2: müssen 1–8 sein
        for (int i = 0; i < 3; i++) {
            if (!ALLOWED_STANDARD_ROTORS.contains(rotors[i])) {
                return false;
            }
        }

        // Wenn ein 4. Wert existiert, muss er 9 oder 10 sein
        if (rotors.length == 4 && !ALLOWED_REFLECTORS.contains(rotors[3])) {
            return false;
        }

        // Prüfe auf eindeutige Werte mittels boolean[] (Index 1–10)
        boolean[] seen = new boolean[11]; // Index 0 wird ignoriert
        for (Integer value : rotors) {
            if (seen[value]) {
                return false; // Duplikat gefunden
            }
            seen[value] = true;
        }

        return true;
    }
}
