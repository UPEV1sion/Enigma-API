package org.api.restObjects.enigma;

import org.api.restObjects.validators.enigma.annotations.*;

import java.util.Arrays;

public record Enigma(
        @ValidEnigmaModel
        String model,
        @ValidEnigmaReflector
        char reflector,
        @ValidEnigmaRotors
        String[] rotors,
        @ValidEnigmaRotorPositions
        String[] positions,
        @ValidEnigmaRotorPositions
        String[] rings,
        @ValidEnigmaPlugboard
        String plugboard,
        @ValidEnigmaInput
        String input,
        String output) {

    public Enigma withOutput(String output) {
        return new Enigma(
                model(),
                reflector(),
                rotors(),
                positions(),
                rings(),
                plugboard(),
                input(),
                output
        );
    }

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
