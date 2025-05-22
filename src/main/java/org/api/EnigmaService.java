package org.api;

import org.api.restObjects.cyclometer.CyclometerRequest;
import org.api.restObjects.enigma.Enigma;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EnigmaService {

    public Enigma normalizeEnigma(Enigma enigma) {
        Integer[] rotors = enigma.rotors();
        Integer[] positions = enigma.positions();
        Integer[] rings = enigma.rings();

        boolean shouldSwap = rotors != null && rotors.length >= 3
                && positions != null && positions.length >= 3
                && rings != null && rings.length >= 3;

        if (shouldSwap) {
            Integer[] newRotors = Arrays.copyOf(rotors, rotors.length);
            Integer[] newPositions = Arrays.copyOf(positions, positions.length);
            Integer[] newRings = Arrays.copyOf(rings, rings.length);

            swap_0_2(newRotors);
            swap_0_2(newPositions);
            swap_0_2(newRings);

            return new Enigma(
                    enigma.model(),
                    enigma.reflector(),
                    newRotors,
                    newPositions,
                    newRings,
                    enigma.plugboard(),
                    enigma.input()
            );
        }
        return enigma;
    }

    private void swap_0_2(Integer[] arr) {
        Integer temp = arr[0];
        arr[0] = arr[2];
        arr[2] = temp;
    }
    public CyclometerRequest normalizeCyclometerRequest(CyclometerRequest req) {
        Enigma enigma = req.enigma();

        if (enigma != null) {
            Enigma normalizedEnigma = normalizeEnigma(enigma);
            return new CyclometerRequest(normalizedEnigma, req.parameters());
        }
        return req;
    }

    public ManualCyclometerRequest normalizeManualCyclometerRequest(ManualCyclometerRequest req) {
        Enigma enigma = req.enigma();

        if (enigma != null) {
            Enigma normalizedEnigma = normalizeEnigma(enigma);
            return new ManualCyclometerRequest(normalizedEnigma, req.parameters());
        }
        return req;
    }
}
