package org.api.restObjects.enigma;

import org.api.restObjects.validators.enigma.annotations.*;

import java.util.Arrays;


@ValidEnigmaRotorSize
public record Enigma(
        @ValidEnigmaModel
        Integer model,
        @ValidEnigmaReflector
        char reflector,
        @ValidEnigmaRotors
        Integer[] rotors,
        @ValidEnigmaRotorPositions
        Integer[] positions,
        @ValidEnigmaRotorPositions
        Integer[] rings,
        @ValidEnigmaPlugboard
        String plugboard,
        @ValidEnigmaInput
        String input) {


    @Override
    public String toString() {
        return "Enigma{" +
                "model='" + model + '\'' +
                ", reflector=" + reflector +
                ", rotors=" + Arrays.toString(rotors) +
                ", positions=" + Arrays.toString(positions) +
                ", rings=" + Arrays.toString(rings) +
                ", plugboard='" + plugboard + '\'' +
                ", input='" + input + '\'' +
                '}';
    }
}
