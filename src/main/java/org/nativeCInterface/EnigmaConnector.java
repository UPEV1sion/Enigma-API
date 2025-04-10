package org.nativeCInterface;

import org.api.restObjects.enigma.Enigma;

import java.util.Optional;

public interface EnigmaConnector {
    Optional<Enigma> getOutputFromEnigma(final Enigma enigma);
}
